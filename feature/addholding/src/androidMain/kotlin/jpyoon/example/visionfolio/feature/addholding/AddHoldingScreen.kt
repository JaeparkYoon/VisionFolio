package jpyoon.example.visionfolio.feature.addholding

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun AddHoldingScreen(
    state: AddHoldingState,
    onIntent: (AddHoldingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VfColors.BgDefault)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        val isEdit = state.editingId != null
        Text(
            stringResource(if (isEdit) R.string.title_edit_holding else R.string.title_add_holding),
            style = VfTypography.TitleScreen,
            color = VfColors.InkPrimary,
        )
        Text(
            stringResource(if (isEdit) R.string.subtitle_edit_holding else R.string.subtitle_add_holding),
            style = VfTypography.MetaSub,
            color = VfColors.InkTertiary,
            modifier = Modifier.padding(top = 4.dp),
        )

        Spacer(Modifier.height(20.dp))
        FieldLabel(stringResource(R.string.label_category))
        CategorySelector(
            selected = state.category,
            onSelect = { onIntent(AddHoldingIntent.SetCategory(it)) },
        )

        Spacer(Modifier.height(16.dp))
        FieldLabel(stringResource(R.string.label_currency))
        CurrencySelector(
            selected = state.currency,
            onSelect = { onIntent(AddHoldingIntent.SetCurrency(it)) },
        )

        Spacer(Modifier.height(16.dp))
        InputField(
            label = stringResource(
                when {
                    state.category.isBond -> R.string.label_bond_name
                    else -> R.string.label_stock_name
                }
            ),
            value = state.name,
            onChange = { onIntent(AddHoldingIntent.SetName(it)) },
        )

        when {
            state.category.isCash -> {
                Spacer(Modifier.height(12.dp))
                InputField(
                    label = stringResource(R.string.label_amount),
                    value = state.quantity,
                    onChange = { onIntent(AddHoldingIntent.SetQuantity(it)) },
                    keyboardType = KeyboardType.Decimal,
                )
            }
            state.category.isBond -> {
                Spacer(Modifier.height(12.dp))
                InputField(
                    label = stringResource(R.string.label_stock_code_optional),
                    value = state.code,
                    onChange = { onIntent(AddHoldingIntent.SetCode(it)) },
                )

                Spacer(Modifier.height(12.dp))
                InputField(
                    label = stringResource(R.string.label_total_valuation),
                    value = state.currentPrice,
                    onChange = { onIntent(AddHoldingIntent.SetCurrentPrice(it)) },
                    keyboardType = KeyboardType.Decimal,
                )

                Spacer(Modifier.height(12.dp))
                InputField(
                    label = stringResource(R.string.label_maturity_date),
                    value = state.maturityDate,
                    onChange = { onIntent(AddHoldingIntent.SetMaturityDate(it)) },
                )
            }
            else -> {
                Spacer(Modifier.height(12.dp))
                InputField(
                    label = stringResource(R.string.label_stock_code_optional),
                    value = state.code,
                    onChange = { onIntent(AddHoldingIntent.SetCode(it)) },
                )

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InputField(
                        label = stringResource(R.string.label_quantity),
                        value = state.quantity,
                        onChange = { onIntent(AddHoldingIntent.SetQuantity(it)) },
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f),
                    )
                    InputField(
                        label = stringResource(R.string.label_current_price),
                        value = state.currentPrice,
                        onChange = { onIntent(AddHoldingIntent.SetCurrentPrice(it)) },
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        state.error?.let { err ->
            Text(
                err,
                color = VfColors.Down,
                style = VfTypography.MetaSub,
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { onIntent(AddHoldingIntent.Dismiss) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VfColors.BgAlt,
                    contentColor = VfColors.InkSecondary,
                ),
                modifier = Modifier.weight(1f),
            ) { Text(stringResource(R.string.btn_cancel)) }

            Button(
                onClick = { onIntent(AddHoldingIntent.Submit) },
                enabled = state.canSubmit && !state.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VfColors.InkPrimary,
                    contentColor = VfColors.BgDefault,
                    disabledContainerColor = VfColors.InkMuted,
                ),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    if (state.isSubmitting) {
                        stringResource(if (isEdit) R.string.btn_saving else R.string.btn_adding)
                    } else {
                        stringResource(if (isEdit) R.string.btn_save else R.string.btn_add)
                    },
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text,
        style = VfTypography.LabelCaps,
        color = VfColors.InkTertiary,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}

@Composable
private fun CategorySelector(
    selected: AssetCategory,
    onSelect: (AssetCategory) -> Unit,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AssetCategory.entries.forEach { cat ->
            VfChip(
                text = cat.displayName,
                selected = selected == cat,
                onClick = { onSelect(cat) },
            )
        }
    }
}

@Composable
private fun CurrencySelector(
    selected: Currency,
    onSelect: (Currency) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Currency.entries.forEach { cur ->
            VfChip(
                text = cur.name,
                selected = selected == cur,
                onClick = { onSelect(cur) },
            )
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val accent = LocalAccent.current
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = accent.base,
            focusedLabelColor = accent.ink,
            cursorColor = accent.base,
            focusedContainerColor = VfColors.BgDefault,
            unfocusedContainerColor = VfColors.BgDefault,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}
