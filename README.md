# 1.netty-file-parent
该组件基于netty3.6.3实现，具有如下功能：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。
使用简单，只需要引入netty-file-client，即可以实现文件的以上操作。
该组件的代码结构分为两部分，客户端组件（netty-flie-client）和服务端组件（netty-flie-server）。

# 2.netty-file-client

2.1 概述

客户端组件主要提供对外访问服务端组件的接口，提供以下接口：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。

org.lyx.file.client包是该组件的核心包，FileClient类是对外提供接口的工具类。具有以下方法：

1. uploadFile 文件上传，对应文件处理句柄类为：UploadFileClientHandler
2. deleteFile 删除服务端文件，对应文件处理句柄类为：DeleteFileClientHandler
3. replaceFile 替换服务端文件，对应文件处理句柄类为：ReplaceFileClientHandler
4. createThumbPicture 生成缩略图，对应文件处理句柄类为：CreateThumbPictureClientHandler


以上所有句柄类的父类均为UploadFileClientHandler，该类实现了一些共有方法，比如一些公共参数的包装等。

2.2 实现步骤

实现步骤以上传文件为例，其他类似实现。

直接上代码：
```
	/**
     * 文件上传
     * @param file 需要上传的文件
     * @param fileName 文件名称
     * @param thumbMark 是否需要生成缩略图
     * @return
     * @author:landyChris
     */
    public static String uploadFile(File file, String fileName,
            boolean thumbMark) {
        FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory();
        //辅助类。用于帮助我们创建NETTY服务
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        String strThumbMark = Constants.THUMB_MARK_NO;
        if (thumbMark) {
            strThumbMark = Constants.THUMB_MARK_YES;
        }
        //具体处理上传文件逻辑
        uploadFile(bootstrap, FileClientContainer.getHost(),
                FileClientContainer.getPort(), file, fileName, strThumbMark,
                FileClientContainer.getUserName(),
                FileClientContainer.getPassword());
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isCode())) {
            return result.getFilePath();
        }
        return null;
    }
```    
具有三个参数，前面几行代码都是很一些netty的初始化工作，具体看一个私有方法uploadFile，如下代码所示：
```
	private static void uploadFile(ClientBootstrap bootstrap, String host,
            int port, File file, String fileName, String thumbMark,
            String userName, String pwd) {
        //1.构建uri对象
        URI uri = getUri(host, port);
        //2.连接netty服务端
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        //3.异步获取Channel对象
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        //4.初始化文件上传句柄对象
        WrapFileClientHandler handler = new UploadFileClientHandler(host, uri,
                file, fileName, thumbMark, userName, pwd);
        //5.获取Request对象
        HttpRequest request = handler.getRequest();
        //6.获取Http数据处理工厂
        HttpDataFactory factory = getHttpDataFactory();
        //7.进行数据的包装处理，主要是进行上传文件所需要的参数的设置，此时调用的句柄是具体的UploadFileClientHandler对象
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        //8.把request写到管道中，传输给服务端
        channel.write(request);
        //9.做一些关闭资源的动作
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
```    
主要有以下实现步骤：
1. 构建uri对象
2. 连接netty服务端
3. 异步获取Channel对象
4. 初始化文件上传句柄对象
5. 获取Request对象
6. 获取Http数据处理工厂
7. 进行数据的包装处理，主要是进行上传文件所需要的参数的设置，此时调用的句柄是具体的UploadFileClientHandler对象
8. 把request写到管道中，传输给服务端
9. 做一些关闭资源的动作

# 3.netty-file-server
3.1 概述

服务端组件实现功能也是跟客户端一致，具有以下功能：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。

org.lyx.file.server包是该组件的核心包。具体的处理句柄类有以下几个：

1. 文件上传：UploadFileServerHandler
2. 删除文件：DeleteFileServerHandler
3. 替换文件：ReplaceFileServerHandler
4. 生成缩略图：CreateThumbPictureServerHandler

以上所以句柄类的接口均为FileServerProcessor，并且继承了抽象类AbstractFileServerHandler。

3.2 实现步骤

具体实现步骤还是以文件上传为例。

首先org.lyx.file.server.support.FileServerHandler类会持续监听客户端的请求，如果是文件处理动作，则会进入messageReceived方法进行相应的处理逻辑。该类定义了以下成员变量：

```
//http请求
private HttpRequest request;
//是否需要断点续传作业
private boolean readingChunks;
//接收到的文件内容
private final StringBuffer responseContent = new StringBuffer();
//解析收到的文件
private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //16384L
//post请求的解码类,它负责把字节解码成Http请求。
private HttpPostRequestDecoder decoder;
//请求参数
private RequestParam requestParams = new RequestParam();
```

该方法实现中，如果文件大小小于chunked的最小值，则直接进行文件上传操作。否则，需要进行分块处理。然后进行文件上传操作。

以上操作主要有两个注意点：

1. 请求参数的解析工作（根据HttpDataType进行相应参数的赋值操作）
2. 根据解析的参数进行相应的文件处理操作（根据文件操作类型，选择相应的处理句柄进行文件处理）

