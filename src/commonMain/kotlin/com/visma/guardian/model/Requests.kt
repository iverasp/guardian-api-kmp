package com.visma.guardian.model

import kotlinx.serialization.Serializable

@Serializable public data class Paging(
    val skip: Int,
    val take: Int,
)

@Serializable public data class Sort(
    val orderBy: Int,
    val orderType: Int,
)

@Serializable public data class PaginationRequest(
    val paging: Paging,
)

@Serializable public data class AbsenceSearchRequest(
    val sort: List<Sort>,
    val paging: Paging,
)

@Serializable public data class AnnouncementSearchRequest(
    val searchPhrase: String,
    val sort: List<Sort>,
    val paging: Paging,
)

@Serializable public data class DiaryPostSearchRequest(
    val searchPhrase: String,
    val paging: Paging,
)

@Serializable public data class CreateAbsenceRequest(
    val type: Int,
    val startDate: String,
    val stopDate: String,
)

@Serializable public data class UpdateAbsenceRequest(
    val id: String,
    val type: Int,
    val startDate: String,
    val stopDate: String,
    val version: String,
)

@Serializable public data class ChangeChildRequest(
    val id: String,
)

@Serializable public data class ChangeConsentRequest(
    val consentId: String,
    val approved: Boolean,
)

@Serializable public data class SendMessageRequest(
    val message: String,
)

@Serializable public data class MarkMessageSeenRequest(
    val chatMessageId: String,
)

@Serializable public data class CalendarEventRequest(
    val occurrenceStartDate: String,
    val occurrenceEndDate: String,
)

@Serializable public data class CalendarEventsRequest(
    val statusDate: String,
    val childIds: List<String>? = null,
)

@Serializable public data class CalendarDateRange(
    val from: String,
    val to: String,
)

@Serializable public data class CalendarCountsRequest(
    val dateRange: CalendarDateRange,
    val childIds: List<String>? = null,
)

@Serializable public data class CreateSessionRequest(
    val vismaConnectClientContext: String,
    val languageCode: String,
    val deviceId: String,
)

@Serializable public data class RegisterDeviceRequest(
    val deviceId: String,
    val applicationName: String = "com.visma.mykindergarten.guardian.data",
    val applicationVersion: String = "1.2.26",
    val deviceSystemType: Int = 0,
)
