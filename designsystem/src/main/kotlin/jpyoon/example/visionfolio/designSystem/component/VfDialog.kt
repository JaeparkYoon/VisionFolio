package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors

@Composable
fun VfDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    confirmText: String = "확인",
    dismissText: String = "취소",
    content: @Composable () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = VfColors.Card),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VfColors.InkPrimary,
                )
                Spacer(Modifier.height(16.dp))
                content()
                Spacer(Modifier.height(24.dp))
                val accent = LocalAccent.current
                if (onConfirm != null) {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = accent.base),
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(confirmText)
                    }
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(dismissText, color = VfColors.InkSecondary)
                }
            }
        }
    }
}
