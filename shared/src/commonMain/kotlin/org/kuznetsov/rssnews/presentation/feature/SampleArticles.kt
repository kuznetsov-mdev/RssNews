package org.kuznetsov.rssnews.presentation.feature

import org.kuznetsov.rssnews.presentation.model.ArticleUi

/** Sample data for screen previews, matching the Broadsheet mock content. */
internal val sampleArticles = listOf(
    ArticleUi(
        id = "1",
        category = "Climate",
        headline = "Rivers reroute as the delta drains a second summer",
        byline = "Nadia Ferreira · 6 min read",
        isFavorite = false,
        body = "The delta's channels have shifted twice since spring, leaving fishing " +
            "villages to rebuild docks around a coastline that no longer matches the map. " +
            "Engineers say the pattern will likely repeat every dry season from here on.",
    ),
    ArticleUi(
        id = "2",
        category = "Economy",
        headline = "Central bank holds rates, signals a long pause ahead",
        byline = "Marcus Lund · 4 min read",
        isFavorite = true,
        body = "Policymakers voted unanimously to hold the benchmark rate, citing easing " +
            "inflation and a labour market that has cooled without cracking.",
    ),
    ArticleUi(
        id = "3",
        category = "Health",
        headline = "Rural clinics test a two-day week for specialists",
        byline = "Erin Kovač · 3 min read",
        isFavorite = true,
        body = "A pilot programme is rotating visiting specialists between four rural " +
            "counties, aiming to cut wait times without adding new hires.",
    ),
)
