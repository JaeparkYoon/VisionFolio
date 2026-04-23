package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRangeSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: (from: Long, to: Long) -> Unit,
) {
    val zone = remember { ZoneId.systemDefault() }
    val today = remember { LocalDate.now() }
    var startDate by rememberSaveable(
        stateSaver = LocalDateSaver,
    ) { mutableStateOf(today.minusMonths(1)) }
    var endDate by rememberSaveable(
        stateSaver = LocalDateSaver,
    ) { mutableStateOf(today) }

    val isValid = !endDate.isBefore(startDate)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            // Headline
            Text(
                text = stringResource(R.string.period_custom),
                style = VfTypography.LabelCaps,
                color = VfColors.InkTertiary,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${formatDate(startDate)}  —  ${formatDate(endDate)}",
                style = VfTypography.TitleScreen,
                color = VfColors.InkPrimary,
            )

            Spacer(Modifier.height(24.dp))

            DateTapRow(
                label = "시작일",
                date = startDate,
                onChange = { startDate = it },
            )

            Spacer(Modifier.height(20.dp))

            DateTapRow(
                label = "종료일",
                date = endDate,
                onChange = { endDate = it },
            )

            if (!isValid) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "종료일은 시작일 이후여야 해요.",
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                ) { Text(stringResource(R.string.btn_cancel), color = VfColors.InkSecondary) }

                Button(
                    onClick = {
                        val from = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
                        val to = endDate.atTime(23, 59, 59).atZone(zone).toInstant().toEpochMilli()
                        onConfirm(from, to)
                    },
                    enabled = isValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VfColors.InkPrimary,
                        contentColor = VfColors.BgDefault,
                        disabledContainerColor = VfColors.InkMuted,
                    ),
                    modifier = Modifier.weight(1f),
                ) { Text(stringResource(R.string.btn_apply)) }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DateTapRow(
    label: String,
    date: LocalDate,
    onChange: (LocalDate) -> Unit,
) {
    val currentYear = remember { LocalDate.now().year }

    Column {
        Text(label, style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SegmentDropdown(
                value = "${date.year}년",
                options = (currentYear - 20..currentYear + 1).toList().reversed(),
                optionText = { "${it}년" },
                onSelect = { onChange(safelyChangeYear(date, it)) },
                modifier = Modifier.weight(1.2f),
            )
            SegmentDropdown(
                value = "${date.monthValue}월",
                options = (1..12).toList(),
                optionText = { "${it}월" },
                onSelect = { onChange(safelyChangeMonth(date, it)) },
                modifier = Modifier.weight(1f),
            )
            SegmentDropdown(
                value = "${date.dayOfMonth}일",
                options = (1..date.lengthOfMonth()).toList(),
                optionText = { "${it}일" },
                onSelect = { onChange(date.withDayOfMonth(it)) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun <T> SegmentDropdown(
    value: String,
    options: List<T>,
    optionText: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VfColors.BgAlt)
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = value,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = VfColors.InkSecondary,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = 280.dp)
                .background(Color.White),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionText(option), color = VfColors.InkPrimary) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun safelyChangeYear(date: LocalDate, year: Int): LocalDate {
    val candidate = date.withYear(year)
    val maxDay = candidate.lengthOfMonth()
    return if (date.dayOfMonth > maxDay) candidate.withDayOfMonth(maxDay) else candidate
}

private fun safelyChangeMonth(date: LocalDate, month: Int): LocalDate {
    val candidate = date.withMonth(month)
    val maxDay = candidate.lengthOfMonth()
    return if (date.dayOfMonth > maxDay) candidate.withDayOfMonth(maxDay) else candidate
}

private fun formatDate(date: LocalDate): String =
    "${date.year}.${date.monthValue}.${date.dayOfMonth}"

private val LocalDateSaver = androidx.compose.runtime.saveable.Saver<LocalDate, String>(
    save = { it.toString() },
    restore = { LocalDate.parse(it) },
)
