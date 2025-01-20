
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.arb.soulscribe.R
import com.arb.soulscribe.navigation.AddEditScreen
import com.arb.soulscribe.navigation.LockScreen
import com.arb.soulscribe.navigation.PasswordScreen
import com.arb.soulscribe.ui_Layer.States.JournalUiState
import com.arb.soulscribe.ui_Layer.utils.WalkthroughManager

@Composable
fun WalkThroughScreenUI(navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121025)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.homepage),
            contentDescription = null,
            modifier = Modifier
                .size(screenWidth * 0.9f)
                .padding(top = screenHeight * 0.1f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.05f))

        Text(
            text = "Welcome to SoulScribe",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(top = screenHeight * 0.02f)
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.05f))

        Column(modifier = Modifier.fillMaxWidth()) {
            listOf(
                Icons.Rounded.Person to "Write about your day, save your experience, and organize your life.",
                Icons.Rounded.Lock to "Lock your journal to keep it private.",
                Icons.Rounded.Star to "Capture your thoughts, reflect, and grow."
            ).forEach { (icon, text) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidth * 0.05f, vertical = screenHeight * 0.01f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(screenWidth * 0.1f)
                    )
                    Spacer(modifier = Modifier.width(screenWidth * 0.04f))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(screenHeight * 0.09f))

        Button(
            onClick = {    WalkthroughManager.setWalkthroughShown(context)
                navController.navigate(LockScreen


                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidth * 0.1f)
                .height(screenHeight * 0.06f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5854D4)),
            shape = RoundedCornerShape(13.dp)
        ) {
            Text(
                "Continue",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )
        }
    }
}
