package jpyoon.example.visionfolio.feature.upload.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.AssetBadge
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.AssetCategory.CASH
import jpyoon.example.visionfolio.domain.model.AssetCategory.PENSION
import androidx.compose.ui.res.stringResource
import jpyoon.example.visionfolio.designsystem.R

@Composable
fun ConfidenceLabel(
    count: Int,
    confidence: Float,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.confidence_label_format, count, "%.0f".format(confidence * 100)),
        style = VfTypography.MetaSub,
        color = VfColors.InkTertiary,
        modifier = modifier,
    )
}

@Composable
fun ParsedRowList(
    parsed: List<ParsedHolding>,
    onToggle: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        parsed.forEachIndexed { index, row ->
            if (index > 0) HorizontalDivider(color = VfColors.LineSoft)
            ParsedRow(
                row = row,
                onToggle = { onToggle(index) },
                onEdit = { onEdit(index) },
            )
        }
    }
}

@Composable
private fun ParsedRow(
    row: ParsedHolding,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
) {
    val accent = LocalAccent.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = row.selected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = accent.base),
            modifier = Modifier.size(22.dp),
        )
        AssetBadge(
            category = row.category,
            initial = row.name.take(1),
            modifier = Modifier.padding(start = 12.dp),
            imageUrl = row.code.toLogoUrl(row.category),
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
        ) {
            Text(
                row.name,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
                maxLines = 1,
            )
            when {
                row.category.isCash -> {
                    Text(
                        text = stringResource(
                            R.string.parsed_row_cash_detail,
                            row.category.displayName,
                            CurrencyFormatter.format(row.quantity.toLong(), row.currency, compact = true),
                        ),
                        style = VfTypography.MetaSub,
                        color = VfColors.InkTertiary,
                        maxLines = 1,
                    )
                }
                row.category.isBond -> {
                    val detail = buildString {
                        append(row.category.displayName)
                        append(" · ")
                        append(CurrencyFormatter.format(row.currentValue.toLong(), row.currency, compact = true))
                        if (!row.maturityDate.isNullOrBlank()) {
                            append(" · ")
                            append(row.maturityDate)
                        }
                    }
                    Text(
                        text = detail,
                        style = VfTypography.MetaSub,
                        color = VfColors.InkTertiary,
                        maxLines = 1,
                    )
                }
                else -> {
                    Text(
                        text = stringResource(
                            R.string.parsed_row_detail,
                            row.category.displayName,
                            formatQuantity(row.quantity, stringResource(R.string.unit_shares)),
                            CurrencyFormatter.format(row.currentValue.toLong(), row.currency, compact = true),
                        ),
                        style = VfTypography.MetaSub,
                        color = VfColors.InkTertiary,
                        maxLines = 1,
                    )
                }
            }
        }
        Icon(
            Icons.Outlined.Edit,
            contentDescription = stringResource(R.string.cd_edit),
            tint = VfColors.InkSecondary,
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onEdit),
        )
    }
}

private fun formatQuantity(q: Double, suffix: String): String =
    if (q % 1.0 == 0.0) "${q.toLong()}$suffix" else "%.4f".format(q)

private fun String.toLogoUrl(category: AssetCategory): String? =
    if (isBlank() || category == CASH || category == PENSION) null
    else "https://img.logokit.com/ticker/$this"
