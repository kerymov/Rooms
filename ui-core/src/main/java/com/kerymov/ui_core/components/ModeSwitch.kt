import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_core.components.UniversalIcon
import com.kerymov.ui_core.theme.RoomsTheme
import com.kerymov.ui_core.utils.IconSource

@Immutable
data class ModeSwitchColors(
    val containerColor: Color,
    val indicatorColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledIndicatorColor: Color,
    val disabledSelectedContentColor: Color,
    val disabledUnselectedContentColor: Color,
)

object ModeSwitchDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.onPrimary,
        indicatorColor: Color = MaterialTheme.colorScheme.primary,
        selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        unselectedContentColor: Color = MaterialTheme.colorScheme.outlineVariant,
        disabledContainerColor: Color = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
        disabledIndicatorColor: Color = MaterialTheme.colorScheme.primary.copy(0.5f),
        disabledSelectedContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(0.5f),
        disabledUnselectedContentColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(0.5f),
    ) = ModeSwitchColors(
        containerColor = containerColor,
        indicatorColor = indicatorColor,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        disabledContainerColor = disabledContainerColor,
        disabledIndicatorColor = disabledIndicatorColor,
        disabledSelectedContentColor = disabledSelectedContentColor,
        disabledUnselectedContentColor = disabledUnselectedContentColor,
    )
}

@Composable
fun ModeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leftIcon: IconSource,
    rightIcon: IconSource,
    modifier: Modifier = Modifier,
    leftContentDescription: String = "Left option",
    rightContentDescription: String = "Right option",
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ModeSwitchColors = ModeSwitchDefaults.colors()
) {
    val transition = updateTransition(targetState = checked, label = "ModeSwitch")

    val indicatorOffset by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "IndicatorOffset"
    ) { isChecked ->
        if (isChecked) 1f else 0f
    }

    val leftIconColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) },
        label = "LeftIconColor"
    ) { isChecked ->
        if (!isChecked) colors.selectedContentColor else colors.unselectedContentColor
    }

    val rightIconColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 200) },
        label = "RightIconColor"
    ) { isChecked ->
        if (isChecked) colors.selectedContentColor else colors.unselectedContentColor
    }

    val leftIconScale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessHigh
            )
        },
        label = "LeftIconScale"
    ) { isChecked ->
        if (!isChecked) 1.1f else 0.9f
    }

    val rightIconScale by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessHigh
            )
        },
        label = "RightIconScale"
    ) { isChecked ->
        if (isChecked) 1.1f else 0.9f
    }

    BoxWithConstraints(
        modifier = modifier
            .height(48.dp)
            .width(120.dp)
            .clip(shape)
            .background(if (enabled) colors.containerColor else colors.disabledContainerColor)
            .toggleable(
                value = checked,
                onValueChange = { onCheckedChange(!checked) },
                role = Role.Switch,
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .offset(x = (maxWidth / 2.0f * indicatorOffset))
                .clip(shape)
                .background(if (enabled) colors.indicatorColor else colors.disabledIndicatorColor),
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            UniversalIcon(
                iconSource = leftIcon,
                contentDescription = leftContentDescription,
                tint = leftIconColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(leftIconScale)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            UniversalIcon(
                iconSource = rightIcon,
                contentDescription = rightContentDescription,
                tint = rightIconColor,
                modifier = Modifier
                    .size(24.dp)
                    .scale(rightIconScale)
            )
        }
    }
}

@Preview
@Composable
private fun ModeSwitchPreview() {
    RoomsTheme {
        var value by remember { mutableStateOf(true) }

        ModeSwitch(
            checked = value,
            onCheckedChange = { value = it },
            leftIcon = IconSource.Vector(Icons.Rounded.Timer),
            rightIcon = IconSource.Vector(Icons.Rounded.Keyboard),
            leftContentDescription = "Manual Mode",
            rightContentDescription = "Timer Mode"
        )
    }
}