package life.majiang.community.community.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CloudProvider {
    @Value("${cloud.secret-id}")
    String secretId;

    @Value("${cloud.secret-key}")
    String secretKey;

    @Value("${cloud.region-qy}")
    String regionQY;

    @Value("${cloud.bucket-name}")
    String bucketName;

    @Value("${cloud.expires}")
    Integer expires;

    public String upload(InputStream inputStream, String contentType, String fileName) {
        //    private String secretId = "AKIDg7LCqNIm5MYJ6gbledNtESZQ2FSR26RV";
//    private String secretKey = "6wa5JegRwCCwb4JHB2dy1nWixvdRxWor";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionQY);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        // 指定要上传的文件
//        File localFile = new File(localFilePath);
        String generateFileName = "";
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generateFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        }
        // 指定要上传到的存储桶

        // 指定要上传到 COS 上对象键
        String key = generateFileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata());
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,key,fileStream);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        if (putObjectResult == null) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        // 获取链接
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        // 设置签名过期时间(可选), 若未进行设置则默认使用ClientConfig中的签名过期时间(1小时)
        // 这里设置签名在1天后过期
        Date expirationDate = new Date(System.currentTimeMillis() + expires);
        req.setExpiration(expirationDate);

        URL url = cosClient.generatePresignedUrl(req);
        cosClient.shutdown();
        return url.toString();
    }


//    private void shutdown() {
//        // 关闭客户端(关闭后台线程)
//        cosClient.shutdown();
//    }

    /**
     * 删除
     */
//    private void delete() {
//        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
//        String bucketName = "examplebucket-1250000000";
//        String key = "exampleobject";
//        cosClient.deleteObject(bucketName, key);
//    }

    /**
     * 下载对象
     */
//    private void download() {
//        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
//        String bucketName = "examplebucket-1250000000";
//        String key = "exampleobject";
//        // 方法1 获取下载输入流
//        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
//        COSObject cosObject = cosClient.getObject(getObjectRequest);
//        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
//
//        // 方法2 下载文件到本地
//        String outputFilePath = "exampleobject";
//        File downFile = new File(outputFilePath);
//        getObjectRequest = new GetObjectRequest(bucketName, key);
//        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
//    }

    /**
     * 查询存储桶中对象列表
     */
//    private void query() {
//        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
//        String bucketName = "examplebucket-1250000000";
//        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
//        // 设置bucket名称
//        listObjectsRequest.setBucketName(bucketName);
//        // prefix表示列出的object的key以prefix开始
//        listObjectsRequest.setPrefix("images/");
//        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
//        listObjectsRequest.setDelimiter("/");
//        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
//        listObjectsRequest.setMaxKeys(1000);
//        ObjectListing objectListing = null;
//        do {
//            try {
//                objectListing = cosClient.listObjects(listObjectsRequest);
//            } catch (CosServiceException e) {
//                e.printStackTrace();
//                return;
//            } catch (CosClientException e) {
//                e.printStackTrace();
//                return;
//            }
//            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
//            List<String> commonPrefixs = objectListing.getCommonPrefixes();
//
//            // object summary表示所有列出的object列表
//            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
//            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
//                // 文件的路径key
//                String key = cosObjectSummary.getKey();
//                // 文件的etag
//                String etag = cosObjectSummary.getETag();
//                // 文件的长度
//                long fileSize = cosObjectSummary.getSize();
//                // 文件的存储类型
//                String storageClasses = cosObjectSummary.getStorageClass();
//            }
//
//            String nextMarker = objectListing.getNextMarker();
//            listObjectsRequest.setMarker(nextMarker);
//        } while (objectListing.isTruncated());
//    }

    /**
     * 创建存储桶
     *
     * @return
     */
//    private Bucket createBucket() {
//        String bucket = "kqi-1257250651"; //存储桶名称，格式：BucketName-APPID
//        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
//        // 设置 bucket 的权限为 Private(私有读写), 其他可选有公有读私有写, 公有读写
//        createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
//        Bucket bucketResult = null;
//        try {
//            bucketResult = cosClient.createBucket(createBucketRequest);
//        } catch (CosServiceException serverException) {
//            serverException.printStackTrace();
//        } catch (CosClientException clientException) {
//            clientException.printStackTrace();
//        }
//        return bucketResult;
//    }

    /**
     * 查询存储桶列表
     */
//    private void queryBuckets() {
//        List<Bucket> buckets = cosClient.listBuckets();
//        for (Bucket bucketElement : buckets) {
//            String bucketName = bucketElement.getName();
//            String bucketLocation = bucketElement.getLocation();
//        }
//    }
}
