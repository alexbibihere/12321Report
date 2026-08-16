package cn.complaint.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 无UI，直接处理逻辑
        processComplaint()
        // 处理完成后关闭Activity
        finish()
    }

    private fun processComplaint() {
        // 1. 读取剪贴板
        val phoneNumber = extractPhoneNumberFromClipboard()

        if (phoneNumber == null) {
            showToast("未检测到有效手机号码")
            return
        }

        // 2. 将号码复制回剪贴板（方便用户在投诉页面粘贴）
        copyToClipboard(phoneNumber)

        // 3. 打开投诉页面
        openComplaintPage(phoneNumber)
    }

    private fun extractPhoneNumberFromClipboard(): String? {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
        val clipData = clipboardManager?.primaryClip

        if (clipData == null || clipData.itemCount == 0) {
            return null
        }

        val text = clipData.getItemAt(0).text?.toString() ?: return null

        // 正则提取11位手机号
        // 支持: 13812345678, +86 13812345678, 138-1234-5678 等
        val phonePattern = Regex(
            """(?:\\+?86)?[-\\s]*(1[3-9]\\d)[-.\s]*(\\d{4})[-.\s]*(\\d{4})"""
        )

        val match = phonePattern.find(text)
        if (match != null) {
            // 提取纯净的11位号码
            val cleanNumber = match.groupValues[1] + match.groupValues[2] + match.groupValues[3]
            return cleanNumber
        }

        return null
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as? android.content.ClipboardManager
        val clipData = android.content.ClipData.newPlainText("phone_number", text)
        clipboardManager?.setPrimaryClip(clipData)
    }

    private fun openComplaintPage(phoneNumber: String) {
        // 构造投诉URL（尝试带参数）
        val complaintUrl = "https://wechat.12321.cn/harass"

        // 优先尝试使用微信打开
        val wechatPackage = "com.tencent.mm"
        val wechatIntent = Intent().apply {
            setPackage(wechatPackage)
            action = Intent.ACTION_VIEW
            data = Uri.parse(complaintUrl)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // 检查微信是否安装
        val packageManager = packageManager
        val hasWechat = try {
            packageManager.getPackageInfo(wechatPackage, 0)
            true
        } catch (e: Exception) {
            false
        }

        if (hasWechat && wechatIntent.resolveActivity(packageManager) != null) {
            showToast("号码已准备，请在页面中粘贴: $phoneNumber")
            startActivity(wechatIntent)
        } else {
            // 降级到系统浏览器
            showToast("使用浏览器打开投诉页面，号码: $phoneNumber")
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(complaintUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(browserIntent)
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}
