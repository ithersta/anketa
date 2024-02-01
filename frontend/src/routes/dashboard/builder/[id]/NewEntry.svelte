<script lang="ts">
    import * as Card from "$lib/components/ui/card";

    import { Button } from "$lib/components/ui/button";
    import {
        CheckCheck, CircleDot, Ruler, TextCursorInput, TextIcon,
    } from "lucide-svelte";
    import type { SurveyEntryBuilderUiState } from "$lib/survey/entries";
    import { Text } from "$lib/survey/text";
    import { TextField } from "$lib/survey/textfield";
    import { MultiChoice } from "$lib/survey/multichoice";
    import { PolarChoice } from "$lib/survey/polarchoice";

    export let openEditor: (uiState: SurveyEntryBuilderUiState) => void
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>Добавить блок</Card.Title>
    </Card.Header>
    <Card.Content>
        <div class="grid grid-cols-2 gap-2">
            <Button variant="secondary" on:click={() => openEditor(Text.Builder.toUiState(undefined))}>
                <TextIcon class="mr-2 h-4 w-4" />
                Текст
            </Button>
            <Button variant="secondary" on:click={() => openEditor(TextField.Builder.toUiState(undefined))}>
                <TextCursorInput class="mr-2 h-4 w-4" />
                Текстовое поле
            </Button>
            <Button variant="secondary" on:click={() => openEditor(MultiChoice.Builder.toUiState(undefined))}>
                <CheckCheck class="mr-2 h-4 w-4" />
                Множественный выбор
            </Button>
            <Button variant="secondary" on:click={() => openEditor(MultiChoice.Builder.toUiState(undefined, true))}>
                <CircleDot class="mr-2 h-4 w-4" />
                Одиночный выбор
            </Button>
            <Button variant="secondary" on:click={() => openEditor(PolarChoice.Builder.toUiState(undefined))}>
                <Ruler class="mr-2 h-4 w-4" />
                Анкета полярных профилей
            </Button>
        </div>
    </Card.Content>
</Card.Root>