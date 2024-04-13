package com.bunbeauty.fakelivestream.features.stream.view.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.LocalePreview
import com.bunbeauty.fakelivestream.common.ui.components.CachedImage
import com.bunbeauty.fakelivestream.common.ui.components.ImageSource
import com.bunbeauty.fakelivestream.common.ui.components.bottomsheet.FakeLiveBottomSheet
import com.bunbeauty.fakelivestream.common.ui.components.bottomsheet.FakeLiveBottomSheetContent
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.theme.bold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
sealed interface QuestionState {
    @Immutable
    data object Hidden : QuestionState

    @Immutable
    data object Empty : QuestionState

    @Immutable
    data class NotEmpty(
        val questions: ImmutableList<QuestionUi>,
    ) : QuestionState
}

@Immutable
data class QuestionUi(
    val uuid: String,
    val picture: ImageSource<*>,
    val username: String,
    val text: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsBottomSheet(
    show: Boolean,
    onDismissRequest: () -> Unit,
    questionState: QuestionState,
    modifier: Modifier = Modifier,
) {
    if (show) {
        FakeLiveBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest
        ) {
            QuestionsContent(questionState = questionState)
        }
    }
}

@Composable
private fun ColumnScope.QuestionsContent(questionState: QuestionState) {
    FakeLiveBottomSheetContent(titleResId = R.string.stream_questions_title) {
        when (questionState) {
            QuestionState.Empty -> {
                EmptyBottomSheetContent(
                    bodyResId = R.string.stream_questions_body,
                    descriptionResId = R.string.stream_questions_description,
                )
            }

            is QuestionState.NotEmpty -> {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.stream_questions_tap_to_answer),
                    color = FakeLiveStreamTheme.colors.onSurface,
                    style = FakeLiveStreamTheme.typography.titleMedium,
                )
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.stream_questions_everyone_watching),
                    color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                    style = FakeLiveStreamTheme.typography.bodyMedium,
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .height(320.dp),
                    verticalArrangement = spacedBy(8.dp),
                ) {
                    items(questionState.questions) { question ->
                        QuestionItem(question = question)
                    }
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun QuestionItem(
    question: QuestionUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, FakeLiveStreamTheme.colors.border, RoundedCornerShape(2.dp))
            .padding(horizontal = 8.dp)
            .padding(top = 12.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CachedImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            imageSource = question.picture,
            cacheKey = question.username,
            contentDescription = "Comment avatar",
        )
        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = spacedBy(4.dp)
        ) {
            Row(horizontalArrangement = spacedBy(12.dp)) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = question.username,
                    color = FakeLiveStreamTheme.colors.onSurface,
                    style = FakeLiveStreamTheme.typography.titleSmall,
                )
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = question.text,
                    color = FakeLiveStreamTheme.colors.onSurface,
                    style = FakeLiveStreamTheme.typography.bodySmall,
                )
            }
            Row(horizontalArrangement = spacedBy(16.dp)) {
                Text(
                    text = stringResource(R.string.stream_questions_delete),
                    color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                    style = FakeLiveStreamTheme.typography.bodySmall.bold,
                )
                Text(
                    text = stringResource(R.string.stream_questions_report),
                    color = FakeLiveStreamTheme.colors.onSurfaceVariant,
                    style = FakeLiveStreamTheme.typography.bodySmall.bold,
                )
            }
        }
    }
}

@LocalePreview
@Composable
private fun EmptyQuestionsBottomSheetPreview() {
    FakeLiveStreamTheme {
        Column {
            QuestionsContent(
                questionState = QuestionState.Empty,
            )
        }
    }
}

@LocalePreview
@Composable
private fun NotEmptyQuestionsBottomSheetPreview() {
    FakeLiveStreamTheme {
        Column {
            QuestionsContent(
                questionState = QuestionState.NotEmpty(
                    questions = persistentListOf(
                        QuestionUi(
                            uuid = "1",
                            picture = ImageSource.ResId(R.drawable.a1),
                            username = "user.name",
                            text = "short question",
                        ),
                        QuestionUi(
                            uuid = "2",
                            picture = ImageSource.ResId(R.drawable.a2),
                            username = "user.name2",
                            text = "Looooooooooooooooooooooooooooooooooooo ooooo ooo ooong question",
                        ),
                    )
                ),
            )
        }
    }
}