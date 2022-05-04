package bruhcollective.itaysonlab.jetispot.ui.dac.components_home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bruhcollective.itaysonlab.jetispot.ui.ext.compositeSurfaceElevation
import bruhcollective.itaysonlab.jetispot.ui.shared.MediumText
import bruhcollective.itaysonlab.jetispot.ui.shared.PreviewableAsyncImage
import bruhcollective.itaysonlab.jetispot.ui.shared.Subtext
import bruhcollective.itaysonlab.jetispot.ui.shared.dynamic_blocks.DynamicLikeButton
import bruhcollective.itaysonlab.jetispot.ui.shared.dynamic_blocks.DynamicPlayButton
import com.spotify.dac.player.v1.proto.PlayCommand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallActionCardBinder(
  navController: NavController,
  title: String,
  subtitle: String,
  navigateUri: String,
  likeUri: String,
  imageUri: String,
  imagePlaceholder: String,
  playCommand: PlayCommand
) {
  Card(containerColor = MaterialTheme.colorScheme.compositeSurfaceElevation(3.dp), modifier = Modifier
    .padding(horizontal = 16.dp)
    .height(120.dp)
    .fillMaxWidth()
    .clickable {
      navController.navigate(navigateUri)
    }) {
    Row {
      PreviewableAsyncImage(imageUrl = imageUri, placeholderType = imagePlaceholder, modifier = Modifier
        .fillMaxHeight()
        .width(120.dp))
      Box(
        Modifier
          .fillMaxSize()
          .padding(16.dp)) {
        Column(Modifier.align(Alignment.TopStart)) {
          MediumText(text = title)
          Subtext(text = subtitle)
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
          DynamicLikeButton(objectUrl = likeUri, Modifier.size(42.dp).align(Alignment.CenterStart))
          DynamicPlayButton(command = playCommand, Modifier.size(42.dp).align(Alignment.CenterEnd))
        }
      }
    }
  }
}