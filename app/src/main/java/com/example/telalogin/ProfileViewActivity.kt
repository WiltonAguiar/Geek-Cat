import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.telalogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvEmail = findViewById<TextView>(R.id.tv_email)
        val tvLife = findViewById<TextView>(R.id.tv_life)
        val tvScore = findViewById<TextView>(R.id.tv_score)
        val btnEditProfile = findViewById<Button>(R.id.btn_edit_profile)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        tvName.text = "Nome: ${document.getString("nome")}"
                        tvEmail.text = "Email: ${document.getString("email")}"
                        tvLife.text = "Vida: ${document.getLong("life")?.toString()}"
                        tvScore.text = "Score: ${document.getLong("score")?.toString()}"
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileView", "Erro ao buscar dados: ", e)
                }
        }

        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }
    }
}
