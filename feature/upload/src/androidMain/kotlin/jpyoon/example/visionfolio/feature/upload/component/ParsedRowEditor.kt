package jpyoon.example.visionfolio.feature.upload.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.ParsedField
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParsedRowEditorSheet(
    row: ParsedHolding,
    sheetState: SheetState,
    onEdit: (ParsedField, String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, containerColor = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Text(stringResource(R.string.title_edit_item), style = VfTypography.TitleScreen, color = VfColors.InkPrimary)

            FieldGroup(
                label = stringResource(if (row.category.isBond) R.string.label_bond_name else R.string.label_stock_name),
                initial = row.name,
            ) { new -> onEdit(ParsedField.NAME, new) }
            when {
                row.category.isCash -> {
                    FieldGroup(
                        label = stringResource(R.string.label_amount),
                        initial = row.quantity.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.QUANTITY, new) }
                }
                row.category.isBond -> {
                    FieldGroup(stringResource(R.string.label_stock_code), row.code) { new -> onEdit(ParsedField.CODE, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_total_purchase_amount),
                        initial = row.avgPrice.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.AVG_PRICE, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_total_valuation),
                        initial = row.currentPrice.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.CURRENT_PRICE, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_maturity_date),
                        initial = row.maturityDate.orEmpty(),
                    ) { new -> onEdit(ParsedField.MATURITY_DATE, new) }
                }
                else -> {
                    FieldGroup(stringResource(R.string.label_stock_code), row.code) { new -> onEdit(ParsedField.CODE, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_quantity),
                        initial = row.quantity.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.QUANTITY, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_avg_price),
                        initial = row.avgPrice.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.AVG_PRICE, new) }
                    FieldGroup(
                        label = stringResource(R.string.label_current_price),
                        initial = row.currentPrice.formatNumber(),
                        keyboardType = KeyboardType.Decimal,
                    ) { new -> onEdit(ParsedField.CURRENT_PRICE, new) }
                }
            }

            Text(
                stringResource(R.string.label_category),
                style = VfTypography.LabelCaps,
                color = VfColors.InkTertiary,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AssetCategory.entries.forEach { cat ->
                    VfChip(
                        text = cat.displayName,
                        selected = row.category == cat,
                        onClick = { onEdit(ParsedField.CATEGORY, cat.name) },
                    )
                }
            }

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VfColors.InkPrimary,
                    contentColor = VfColors.BgDefault,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 8.dp),
            ) { Text(stringResource(R.string.btn_done)) }
        }
    }
}

@Composable
private fun FieldGroup(
    label: String,
    initial: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onCommit: (String) -> Unit,
) {
    var value by rememberSaveable(initial) { mutableStateOf(initial) }
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onCommit(it)
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
    )
}

private fun Double.formatNumber(): String =
    if (this % 1.0 == 0.0) toLong().toString() else toString()
