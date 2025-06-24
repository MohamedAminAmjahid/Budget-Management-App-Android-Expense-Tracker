package esisa.ac.ma.budget.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import esisa.ac.ma.budget.MainActivity;
import esisa.ac.ma.budget.R;
import esisa.ac.ma.budget.dao.UsersDao;
import esisa.ac.ma.budget.entities.User;

public class LoginActivity extends AppCompatActivity {
    private EditText mail;
    private EditText pwd;
    private UsersDao usersDao;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.mail);
        pwd = findViewById(R.id.pwd_txt);
        btnLogin = findViewById(R.id.loginButton);
        usersDao = new UsersDao(getBaseContext());

        /*User user = User.builder()
                .mail("ma.amjahid@gmail.com")
                .password("admin123")
                .build();
        usersDao.insert(user);*/
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("email", true)){

            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(
                (view) -> {
                    login(view);
                });

    }

    public void login(View view) {
        String email = mail.getText().toString();
        String password = pwd.getText().toString();
        User user = usersDao.get(email);

        /*User user1 = User.builder()
                .mail("ma.amjahid@esisa.ac.ma")
                .password("admin123")
                .build();
        usersDao.insert(user1);*/

        if (user != null && user.getPassword().equals(password)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("email", true);
            editor.apply();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            TextView errorTextView = findViewById(R.id.errorTextView);
            errorTextView.setText("Login ou mot de passe incorrect");
            errorTextView.setTextColor(Color.RED);
        }
    }

    private String hashPassword(String password) {
        return password + "123";
    }
    private String hashPassword2(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toHexString(0xFF & digest[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}