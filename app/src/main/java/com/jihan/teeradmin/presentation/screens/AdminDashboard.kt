package com.jihan.teeradmin.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.BellRing
import com.composables.icons.lucide.CircleDollarSign
import com.composables.icons.lucide.Gamepad
import com.composables.icons.lucide.GitCompareArrows
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PhoneCall
import com.composables.icons.lucide.Users
import com.jihan.composeutils.Cx
import com.jihan.teeradmin.Routes
import com.jihan.teeradmin.domain.viewmodel.PaymentViewmodel
import org.koin.androidx.compose.koinViewModel

data class DashboardItem(
    val id: Routes, val title: String, val icon: ImageVector, val color: Color,
    val size: Int?=null
)

@Composable
fun AdminDashboard(
    navigateTo: (Routes) -> Unit = {},
) {

    val paymentViewModel = koinViewModel<PaymentViewmodel>()







    val dashboardItems = listOf(
        DashboardItem(
            id = Routes.UserList,
            title = "Users",
            icon = Lucide.Users,
            color = Cx.gray500,
        ),
        DashboardItem(
            id = Routes.CreateMatchRoute,
            title = "Create Match",
            icon = Lucide.Gamepad,
            color = Cx.blue500,
        ),
        DashboardItem(
            id = Routes.ContactRoute,
            title = "Contacts",
            icon = Lucide.PhoneCall,
            color = Cx.green500,
        ),
        DashboardItem(
            id = Routes.PaymentsRoute,
            title = "Payments (${paymentViewModel.pendingUser})",
            icon = Lucide.CircleDollarSign,
            color = Cx.yellow500,
        ),

        DashboardItem(
            id = Routes.GenerateLinkRoutes,
            title = "Generate Link",
            icon = Lucide.Link,
            color = Cx.purple500,
        ),
        DashboardItem(
            id = Routes.NotificationRoute,
            title = "Notification",
            icon = Lucide.BellRing,
            color = Cx.cyan500,
        ),
        DashboardItem(
            id = Routes.UpdateAppRoute,
            title = "Update App",
            icon = Lucide.GitCompareArrows,
            color = Cx.lime500,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = "Admin Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dashboardItems) { item ->
                DashboardItemCard(
                    item = item, onClick = { navigateTo(item.id) })
            }
        }
    }
}

@Composable
fun DashboardItemCard(
    item: DashboardItem, onClick: () -> Unit
) {




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val text = if (item.size != null) {
                "${item.title} (${item.size})"
            } else {
                item.title
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}