package lib.dehaat.ledger.util

import android.content.Context
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import lib.dehaat.ledger.initializer.LedgerSDK

class DownloadFileUtil @Inject constructor(
    @ApplicationContext val context: Context
) {

    private fun File.createFileIfNotExists() =
        try {
            if (!exists()) {
                createNewFile()
            } else {
                deleteRecursively()
                createNewFile()
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
            false
        }

    private fun createTransferUtilityBuilder() = TransferUtility
        .builder()
        .s3Client(getAWSClient())
        .context(context)
        .build()

    private fun getAWSClient(): AmazonS3Client {
        val s3Client = AmazonS3Client(
            AWSMobileClient.getInstance().credentialsProvider,
            ClientConfiguration()
        )
        if (LedgerSDK.isDebug) {
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1))
        } else {
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1))
        }
        return s3Client
    }

    fun downloadFile(
        file: File,
        fileNameKey: String,
        bucket: String
    ) = try {
        if (file.createFileIfNotExists())
            downloadWithTransferUtility(fileNameKey, file, bucket)
        else
            null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private fun downloadWithTransferUtility(
        fileName: String,
        directory: File,
        bucket: String
    ): TransferObserver {
        val transferUtility = createTransferUtilityBuilder()
        return transferUtility.download(bucket, fileName, directory)
    }
}
