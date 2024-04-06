import type { TextReport } from "$lib/report/text";
import type { TextFieldReport } from "$lib/report/textfield";
import type { PolarChoiceReport } from "$lib/report/polarchoice";
import type { MultiChoiceReport } from "$lib/report/multichoice";

export type ReportEntry = TextReport.Entry |
    TextFieldReport.Entry |
    PolarChoiceReport.Entry |
    MultiChoiceReport.Entry
