package com.upload.file;

import com.upload.file.utils.ScpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("aliyun")
public class TestScp {

    @Test
    public void testScp(){
        ScpClient scp = ScpClient.getInstance();
        scp.putFile("C:\\images\\20180907\\test.png","testsss.png",
                "/home/imgroot/html/upload", null);
//        scp.getFile(remoteFile, localTargetDirectory);
    }
}
