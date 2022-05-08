package com.rcalencar.guidomia.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rcalencar.guidomia.R
import com.rcalencar.guidomia.model.CarAd
import com.rcalencar.guidomia.model.carAdList
import com.rcalencar.guidomia.ui.theme.GuidomiaTheme
import com.rcalencar.guidomia.viewmodel.CarAdListViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuidomiaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GuidomiaScreen()
                }
            }
        }
    }
}

@Composable
fun GuidomiaScreen(carAdListViewModel: CarAdListViewModel = viewModel()) {
    val carAdList by carAdListViewModel.liveData.observeAsState(initial = emptyList())
    val makes by carAdListViewModel.makes.observeAsState(initial = emptyList())
    val models by carAdListViewModel.models.observeAsState(initial = emptyList())
    val selectedMake by carAdListViewModel.selectedMake.observeAsState()
    val selectedModel by carAdListViewModel.selectedModel.observeAsState()
    val selectedItem by carAdListViewModel.selectedItem.observeAsState()

    Column {
        DropdownFilter(
            label = "Any make",
            selected = selectedMake,
            items = makes,
            onSelected = { make -> carAdListViewModel.filterByMakes(make) }
        )
        DropdownFilter(
            label = "Any model",
            selected = selectedModel,
            items = models,
            onSelected = { model -> carAdListViewModel.filterByModel(model) }
        )
        CardAdList(
            adList = carAdList,
            selectedItem = selectedItem,
            onSelected = { ad -> carAdListViewModel.selectItem(ad) }
        )
    }
}

@Composable
fun CardAdList(
    adList: List<CarAd>,
    selectedItem: CarAd?,
    onSelected: (CarAd) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        itemsIndexed(
            items = adList,
        ) { index, carAd ->
            Card(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                backgroundColor = colorResource(id = R.color.lightGray)
            ) {
                CarAd(
                    carAd = carAd,
                    isSelected = carAd == selectedItem || (selectedItem == null && index == 0),
                    onSelected = { onSelected(carAd) },
                )
            }
        }
    }
}

@Preview
@Composable
fun CarAdPreview() {
    CarAd(carAdList(LocalContext.current.assets)[0], isSelected = true, onSelected = { })
}

@Composable
fun CarAd(
    carAd: CarAd,
    isSelected: Boolean = true,
    onSelected: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(enabled = !isSelected) { onSelected() }
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row {
            AsyncImage(
                model = "https://github.com/rcalencar/Guidomia/raw/master/app/src/main/assets/${carAd.image()}",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Ad picture",
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(2f)
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.car_ad_description, carAd.make, carAd.model),
                    color = colorResource(id = R.color.adText),
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(bottom = 2.dp),
                )
                Text(
                    text = stringResource(R.string.car_ad_price, carAd.formattedCustomerPrice()),
                    color = colorResource(id = R.color.adText),
                    modifier = Modifier.padding(bottom = 2.dp),
                )
                Row {
                    for (i in 1..carAd.rating) {
                        Icon(Icons.Filled.Star, contentDescription = "start")
                    }
                    for (i in 1..(5 - carAd.rating)) {
                        Icon(Icons.Outlined.StarOutline, contentDescription = "start")
                    }
                }
            }
            if (!isSelected) {
                Icon(
                    imageVector = if (isSelected) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (isSelected) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    }
                )
            }
        }
        if (isSelected) {
            Text(
                "Pros:",
                color = colorResource(id = R.color.adText),
                modifier = Modifier.padding(start = 32.dp),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
            )
            Text(
                bulletAnnotatedString(list = carAd.prosList),
                color = colorResource(id = R.color.bulletPointText),
                modifier = Modifier.padding(start = 50.dp),
                style = MaterialTheme.typography.body2,
            )
            Text(
                "Cons:",
                color = colorResource(id = R.color.adText),
                modifier = Modifier.padding(start = 32.dp),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
            )
            Text(
                bulletAnnotatedString(list = carAd.consList),
                color = colorResource(id = R.color.bulletPointText),
                modifier = Modifier.padding(start = 50.dp),
                style = MaterialTheme.typography.body2,
            )
        }
    }
}

@Composable
fun bulletAnnotatedString(list: List<String>): AnnotatedString {
    val items =
        if (list.isEmpty()) listOf(stringResource(R.string.empty_list)) else list.filter { it.isNotEmpty() }
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))
    return buildAnnotatedString {
        items.forEach {
            withStyle(style = paragraphStyle) {
                append(bullet)
                append("\t\t")
                append(it)
            }
        }
    }
}

const val BULLET_GAP_WIDTH = 22
const val BULLET_RADIUS = 10

private fun bulletList(list: List<String>, container: TextView) {
    val items = if (list.isEmpty()) listOf(container.context.getString(R.string.empty_list)) else list.filter { it.isNotEmpty() }
    container.text = items.joinTo(SpannableStringBuilder(), System.lineSeparator()) {
        SpannableString(it).apply {
            setSpan(BulletSpan(BULLET_GAP_WIDTH, container.context.getColor(R.color.primaryColor), BULLET_RADIUS), 0, this.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun DropdownFilter(
    label: String = "Label",
    selected: String? = null,
    items: List<String> = emptyList(),
    onSelected: (String?) -> Unit = { }
) {
    val options = listOf(label) + items
    var expanded by remember { mutableStateOf(false) }
    val selectedOptionText = selected ?: options[0]

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        if (index == 0) onSelected(null) else onSelected(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}