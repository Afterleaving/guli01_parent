package com.atguigu.vodtest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class TestVod {

    @Value("${aliyun.vod.file.keyid}")
    private String keyId;

    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        //3.向request对象中设置id值
        request.setVideoId("1338a208ae4646a68a7c4e6a1098fd2e");
        //4.调用初始化对象中的方法，传递request
        return client.getAcsResponse(request);
    }
    /**
     * 根据视频id获取视频播放地址
     */
    @Test
    public void getAddById(){
        //1.创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tJ938eEwf9BkvjzSTcp", "K7DDAxQamNLtUImDGoklXpkdU5wnJh");

        //2.创建获取视频地址request和response
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            //5.获取数据
            response = getPlayInfo(client);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    /**
     * 根据视频id获取视频的凭证
     */
    @Test
    public void getVideoPlayAuth() throws ClientException {
        //1.创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tJ938eEwf9BkvjzSTcp", "K7DDAxQamNLtUImDGoklXpkdU5wnJh");

        //2.创建request、response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

        //3.向request中设置视频的id值
        request.setVideoId("c42b6aeef2e94073873816e78580acf2");

        //4.通过初始化对象传递数据，获取
        response = client.getAcsResponse(request);

        System.out.println("视频的凭证："+response.getPlayAuth());

    }


}
