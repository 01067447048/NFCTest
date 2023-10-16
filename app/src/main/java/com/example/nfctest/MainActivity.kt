package com.example.nfctest

import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.cardemulation.CardEmulation
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nfctest.ui.theme.NFCTestTheme
import java.io.IOException

class MainActivity : ComponentActivity() {

    companion object{
        const val ReaderFlags = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }

    private val roleManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(ROLE_SERVICE) as RoleManager
        } else null
    }

    private val readerCallback = NfcAdapter.ReaderCallback { tag ->
        onTagDiscovered(tag)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NFCTestTheme {
                // A surface container using the 'background' color from the theme

                val myComponentName = ComponentName(this, MyHostApduService::class.java)
                val adapter = NfcAdapter.getDefaultAdapter(this)
                val cardEmulation = CardEmulation.getInstance(adapter)
                var dynamicAids = arrayListOf(getString(R.string.aid_1), getString(R.string.aid_2))
                val aidStat = cardEmulation.registerAidsForService(myComponentName, "other", dynamicAids)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                adapter.enableReaderMode(
                                    this@MainActivity, readerCallback,
                                    ReaderFlags, null
                                )

                                Log.d(MainActivity::class.java.simpleName, "aids : $dynamicAids / stats : $aidStat")
                            }
                        ) {
                            Text(text = "Click For Check Nfc Stats")
                        }

                        Button(
                            onClick = {
                                changeRole()
                            }
                        ) {
                            Text(text = "Change")
                        }
                    }
                }
            }
        }
    }

    private fun changeRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(MainActivity::class.java.simpleName, "change Role")
            roleManager?.createRequestRoleIntent(RoleManager.ROLE_BROWSER)
        } else {
            Log.w(MainActivity::class.java.simpleName, "ChangeRole Under Sdk Q")
        }
    }

    private fun onTagDiscovered(tag: Tag) {
        val isoDep = IsoDep.get(tag) ?: return

        try {
            isoDep.connect()
//            val commandApdu = ByteUtil.selectApdu(SelectAID)
//            val result = isoDep.transceive(commandApdu)
        } catch (t: IOException) {
            t.printStackTrace()
        } finally {
            isoDep.close()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NFCTestTheme {
        Greeting("Android")
    }
}