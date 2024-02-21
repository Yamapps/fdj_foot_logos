package com.fdj.footlogos.league.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fdj.footlogos.league.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    selectedItem: String,
    label: String
) {
    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }
    var listItems by remember(items, selectedItem) {
        mutableStateOf(
            if (selectedItem.isNotEmpty()) {
                items.filter { x -> x.contains(selectedItem.lowercase(), ignoreCase = true) }
            } else {
                emptyList()
            }
        )
    }
    var selectedText by remember(selectedItem) { mutableStateOf(selectedItem) }

    LaunchedEffect(selectedItem){
        selectedText = selectedItem
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = selectedText,
                    label = { Text(label) },
                    onValueChange = {
                        if (!expanded) {
                            expanded = true
                        }
                        selectedText = it
                        listItems = if (it.isNotEmpty()) {
                            items.filter { x -> x.contains(it.lowercase(), ignoreCase = true) }
                        } else {
                            items.toList()
                        }
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier
                        .menuAnchor()
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier
                        .clickable { selectedText = "" }
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.clear)
                )
            }

            if (selectedText.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (listItems.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.no_league_found)) },
                            onClick = {
                                expanded = false
                            }
                        )
                    } else {
                        listItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    focusManager.clearFocus()
                                    selectedText = item
                                    expanded = false
                                    onItemSelected(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropDown(){
    DropDown(items = emptyList(), onItemSelected = {}, selectedItem = "", label = "label")
}
