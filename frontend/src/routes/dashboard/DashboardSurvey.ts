export type DashboardSurvey = {
    id: string,
    title: string,
    createdAt: number,
}

export type Dashboard = {
    mySurveys: DashboardSurvey[],
    sharedSurveys: DashboardSurvey[],
}
