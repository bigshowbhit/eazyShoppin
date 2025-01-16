package hu.bme.aut.U5FT4N.easyshopping.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.easyshopping.R
import hu.bme.aut.U5FT4N.easyshopping.data.ItemCategory
import hu.bme.aut.U5FT4N.easyshopping.data.ShoppingItem
import hu.bme.aut.U5FT4N.easyshopping.ui.screen.money.MoneyViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingViewModel = hiltViewModel(),
    moneyViewModel: MoneyViewModel = hiltViewModel(),
    isInternetAvailable: Boolean, // Receive status// Inject MoneyViewModel
    onNavigateToSummary: (Int, Int, Int) -> Unit,
    onNavigateToMoneyApi: (String) -> Unit // Add callback for MoneyApi navigation
) {
    val coroutineScope = rememberCoroutineScope()
    val itemList by viewModel.getAllItems().collectAsState(emptyList())

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var itemToEdit: ShoppingItem? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shopping List") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Filled.AddCircle, contentDescription = stringResource(R.string.add_item))
                    }
                    IconButton(onClick = { viewModel.clearAllItems() }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_all))
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {

                                val totalItems = itemList.size
                                val foodItemsCount = itemList.count { it.category == ItemCategory.FOOD }
                                val electronicsItemsCount = itemList.count { it.category == ItemCategory.ELECTRONIC }

                                onNavigateToSummary(totalItems, foodItemsCount, electronicsItemsCount)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = stringResource(R.string.summary),
                            tint = Color.Blue
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            if (itemList.isEmpty()) {
                Text(
                    "Empty list", modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                LazyColumn {
                    items(itemList) { item ->
                        ShoppingItemCard(
                            item,
                            moneyViewModel = moneyViewModel, // Pass MoneyViewModel here
                            onItemDelete = { viewModel.removeItem(it) },
                            onItemChecked = { item, checked -> viewModel.updateItem(item.copy(isBought = checked)) },
                            onItemEdit = {
                                itemToEdit = it
                                showAddDialog = true
                            },
                            isInternetAvailable = isInternetAvailable,
                            onNavigateToMoneyApi = { shoppingItem -> onNavigateToMoneyApi(shoppingItem) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        NewItemDialog(viewModel, itemToEdit) {
            showAddDialog = false
            itemToEdit = null
        }
    }
}

@Composable
fun NewItemDialog(
    viewModel: ShoppingViewModel,
    itemToEdit: ShoppingItem? = null,
    onCancel: () -> Unit
) {
    var itemName by remember { mutableStateOf(itemToEdit?.name ?: "") }
    var itemDesc by remember { mutableStateOf(itemToEdit?.description ?: "") }
    var estimatedPrice by remember { mutableStateOf(itemToEdit?.estimatedPrice?.toString() ?: "") }
    var category by remember { mutableStateOf(itemToEdit?.category ?: ItemCategory.FOOD) }
    var isBought by remember { mutableStateOf(itemToEdit?.isBought ?: false) }
    var selectedCurrency by remember { mutableStateOf("USD") } // Default currency
    val availableCurrencies = listOf("USD", "EUR", "GBP", "HUF") // Example currencies

    // Error state variables
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onCancel() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(6.dp)
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    if (itemToEdit == null) "New Item" else "Edit Item",
                    style = MaterialTheme.typography.titleMedium
                )

                // Item name input with validation
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.item_name)) },
                    value = itemName,
                    onValueChange = {
                        itemName = it
                        nameError = if (it.isBlank()) "Item name cannot be empty" else ""
                    },
                    isError = nameError.isNotEmpty()
                )
                if (nameError.isNotEmpty()) {
                    Text(
                        text = nameError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                // Description input (optional, no validation)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.description)) },
                    value = itemDesc,
                    onValueChange = { itemDesc = it }
                )

                // Price input with validation
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.estimated_price)) },
                    value = estimatedPrice,
                    onValueChange = {
                        estimatedPrice = it
                        priceError = if (it.toDoubleOrNull() == null) {
                            "Please enter a valid number"
                        } else {
                            ""
                        }
                    },
                    isError = priceError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (priceError.isNotEmpty()) {
                    Text(
                        text = priceError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                    )
                }

                // Category dropdown using SpinnerSample
                SpinnerSample(
                    list = ItemCategory.values().map { it.name },
                    preselected = category.name,
                    onSelectionChanged = { selectedCategory ->
                        category = ItemCategory.valueOf(selectedCategory)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                SpinnerSample(
                    list = availableCurrencies,
                    preselected = selectedCurrency,
                    onSelectionChanged = { selectedCurrency = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        // Validate before saving
                        if (itemName.isBlank()) {
                            nameError = "Item name cannot be empty"
                        }
                        if (estimatedPrice.toDoubleOrNull() == null) {
                            priceError = "Please enter a valid number"
                        }

                        if (nameError.isEmpty() && priceError.isEmpty()) {
                            if (itemToEdit == null) {
                                viewModel.insertItem(
                                    ShoppingItem(
                                        name = itemName,
                                        description = itemDesc,
                                        estimatedPrice = estimatedPrice.toDoubleOrNull() ?: 0.0,
                                        category = category,
                                        isBought = isBought,
                                        currency = selectedCurrency // Add currency field here
                                    )
                                )
                            } else {
                                val updatedItem = itemToEdit.copy(
                                    name = itemName,
                                    description = itemDesc,
                                    estimatedPrice = estimatedPrice.toDoubleOrNull() ?: 0.0,
                                    category = category,
                                    isBought = isBought,
                                    currency = selectedCurrency // Add currency here
                                )
                                viewModel.updateItem(updatedItem)
                            }
                            onCancel()
                        }
                    }) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}



@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value

    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            list.forEach { listEntry ->
                DropdownMenuItem(
                    onClick = {
                        selected = listEntry
                        expanded = false
                        onSelectionChanged(selected)
                    },
                    text = {
                        Text(
                            text = listEntry,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Start)
                        )
                    },
                )
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    isInternetAvailable: Boolean,
    moneyViewModel: MoneyViewModel,
    onItemDelete: (ShoppingItem) -> Unit,
    onItemChecked: (ShoppingItem, Boolean) -> Unit,
    onItemEdit: (ShoppingItem) -> Unit,
    onNavigateToMoneyApi: (String) -> Unit // Callback for navigation
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        var itemChecked by remember { mutableStateOf(item.isBought) }

        Column(modifier = Modifier
            .padding(20.dp)
            .animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.category.getIcon()),
                    contentDescription = stringResource(R.string.category_icon),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(text = "Category: ${item.category}")
                    Text(text = "Price: $${item.estimatedPrice} (${item.currency})")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = itemChecked,
                        onCheckedChange = {
                            itemChecked = it
                            onItemChecked(item, itemChecked)
                        }
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.delete),
                        modifier = Modifier.clickable { onItemDelete(item) },
                        tint = Color.Red
                    )
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier.clickable { onItemEdit(item) },
                        tint = Color.Gray
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) "Less" else "More"
                        )
                    }
                }
            }

            if (expanded) {
                Column {
                    // Show additional actions
                    Button(
                        onClick = { onNavigateToMoneyApi(item.name) },
                        enabled = isInternetAvailable // Disable button if offline
                    ) {
                        Text(stringResource(R.string.fetch_rates))
                    }

                    // Placeholder: Add more expanded item details if needed
                    Text(text = "Additional details about ${item.name}.")
                }
            }
        }
    }
}
