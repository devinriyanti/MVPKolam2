package id.web.devin.mvckolam.util

import android.app.Activity
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.model.*
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


fun ImageView.loadImage(url:String, progressBar: ProgressBar){
    Picasso.get()
        .load(url)
        .resize(1000,1300)
        .centerCrop()
        .error(R.drawable.baseline_error_24)
        .into(this, object : Callback {
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }
            override fun onError(e: Exception?) {
                progressBar.visibility = View.GONE
            }
        })
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    format.maximumFractionDigits = 0
    format.currency = Currency.getInstance("IDR")
    val formattedAmount = format.format(amount)
    return formattedAmount.replace(Regex("\\.00$"), "")
}

fun calculateTotalYears(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    val currentDate = LocalDate.now()
    val period = Period.between(date, currentDate)
    return period.years
}

fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatDate2(inputDate: String): String {
    val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id","ID"))
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun formatDateTime(inputDate: String):String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    val dateTime = LocalDateTime.parse(inputDate, inputFormatter)
    val formattedDateTime = dateTime.format(outputFormatter)

    return formattedDateTime
}

object EncryptionUtils{
    private const val secretKey = "mySecretKey"
    fun encrypt(text: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val secretKeySpec = SecretKeySpec(generateKey(secretKey), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        val encryptedBytes = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
    }

    private fun generateKey(secretKey: String): ByteArray? {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(secretKey.toByteArray(StandardCharsets.UTF_8))
    }
}

object Global {
    const val SHARED_PREFERENCES = "LOKOWAI"
    const val SHARED_PREF_EMAIL = "EMAIL"
    const val SHARED_PREF_KEY_ROLE = "ROLE"
    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_EMAIL, null)
    }

    fun getRole(context: Context): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE)
            .getString(SHARED_PREF_KEY_ROLE, null)
    }
}

interface AuthControllerListener {
    fun showError(message: String)
}

interface KolamControllerListener {
    fun showError(message: String)
    fun showKolam(kolam: List<Kolam>)
}

interface KolamDetailControllerListener {
    fun showError(message: String)
    fun showKolam(kolam: List<Kolam>)
}

interface PelatihControllerListener {
    fun showError(message: String)
    fun showPelatih(pelatih: List<Pelatih>)
}

interface ProductControllerListener {
    fun showError(message: String)
    fun showProduk(produk: List<Produk>)
}

interface ProfileControllerListener {
    fun showError(message: String)
    fun showProfile(profileData: List<Pengguna>)
    fun updateProfil()
}

interface TransaksiControllerListener {
    fun showError(message: String)
    fun showTransaksi(transaksi: List<Transaction>)
}