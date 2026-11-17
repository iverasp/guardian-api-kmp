package com.visma.guardian.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable public data class ListResponse<T>(
    val resultList: List<T>? = null,
    val totalCount: Int? = null,
)

@Serializable public data class IdResponse(
    val id: String? = null,
)

@Serializable public data class ErrorResponse(
    val message: String? = null,
)

@Serializable public data class Municipality(
    val id: String? = null,
    val minSecurityLevel: Int? = null,
    val name: String? = null,
    val vismaConnectClientContext: String? = null,
)

@Serializable public data class Child(
    val firstName: String? = null,
    val guardianHasParentalResponsibility: Boolean? = null,
    val id: String? = null,
    val kindergartenId: String? = null,
    val kindergartenName: String? = null,
    val lastName: String? = null,
    val placementId: String? = null,
    val profilePictureUrl: String? = null,
    val unitName: String? = null,
)

@Serializable public data class SessionInfo(
    val children: List<Child>? = null,
    val customerId: String? = null,
    val customerKey: String? = null,
    val customerName: String? = null,
    val customerOrganizationNumber: String? = null,
    val firstName: String? = null,
    val languageCode: String? = null,
    val lastName: String? = null,
    val municipalityAccessibilityUrl: String? = null,
    val municipalityPrivacyUrl: String? = null,
    val organizationId: String? = null,
    val organizationName: String? = null,
    val organizationNumber: String? = null,
    val personCreatedDate: String? = null,
)

@Serializable public data class AttendanceWeek(
    val friday: Boolean? = null,
    val monday: Boolean? = null,
    val saturday: Boolean? = null,
    val sunday: Boolean? = null,
    val thursday: Boolean? = null,
    val tuesday: Boolean? = null,
    val wednesday: Boolean? = null,
)

@Serializable public data class AttendanceDays(
    val evenWeek: AttendanceWeek? = null,
    val oddWeek: AttendanceWeek? = null,
)

@Serializable public data class ChildDetails(
    val attendanceDays: AttendanceDays? = null,
    val birthDate: String? = null,
    val firstName: String? = null,
    val id: String? = null,
    val isOddWeek: Boolean? = null,
    val lastName: String? = null,
    val placementSize: Int? = null,
    val profilePictureUrl: String? = null,
    val unitName: String? = null,
)

@Serializable public data class AttendanceLogEntry(
    val checkInDateTime: String? = null,
    val checkOutDateTime: String? = null,
    val id: String? = null,
)

@Serializable public data class AttendanceLogResponse(
    val attendances: List<AttendanceLogEntry>? = null,
)

@Serializable public data class ContactPerson(
    val email: String? = null,
    val firstName: String? = null,
    val hasParentalResponsibility: Boolean? = null,
    val hasPickupPermission: Boolean? = null,
    val id: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val receivesInformation: Boolean? = null,
    val relationDescription: String? = null,
    val relationType: Int? = null,
    val workPhone: String? = null,
)

@Serializable public data class ContactPersonsResponse(
    val currentGuardian: ContactPerson? = null,
    val otherGuardians: List<ContactPerson>? = null,
)

@Serializable public data class FullName(
    val firstName: String? = null,
    val lastName: String? = null,
)

@Serializable public data class Absence(
    val id: String? = null,
    val startDate: String? = null,
    val stopDate: String? = null,
    val type: Int? = null,
    val version: String? = null,
)

@Serializable public data class AbsenceDetails(
    val comment: String? = null,
    val createdDate: String? = null,
    val id: String? = null,
    val isEditable: Boolean? = null,
    val modifiedBy: FullName? = null,
    val startDate: String? = null,
    val stopDate: String? = null,
    val type: Int? = null,
    val version: String? = null,
)

@Serializable public data class Consent(
    val consentId: String? = null,
    val description: String? = null,
    val status: Int? = null,
    val title: String? = null,
)

@Serializable public data class CalendarChild(
    val colorIndex: Int? = null,
    val firstName: String? = null,
    val id: String? = null,
)

@Serializable public data class CalendarEvent(
    val attachmentCount: Int? = null,
    val children: List<CalendarChild>? = null,
    val endDate: String? = null,
    val id: String? = null,
    val isAllDay: Boolean? = null,
    val startDate: String? = null,
    val title: String? = null,
)

@Serializable public data class CalendarRepeatOptions(
    val repeatType: Int? = null,
    val until: String? = null,
)

@Serializable public data class CalendarEventDetails(
    val attachments: List<Attachment>? = null,
    val children: List<CalendarChild>? = null,
    val description: String? = null,
    val endDate: String? = null,
    val id: String? = null,
    val isAllDay: Boolean? = null,
    val repeatOptions: CalendarRepeatOptions? = null,
    val startDate: String? = null,
    val title: String? = null,
)

@Serializable public data class CalendarCounts(
    val events: Int? = null,
)

@Serializable public data class CalendarCountsResponse(
    val counts: Map<String, CalendarCounts>? = null,
)

@Serializable public data class Attachment(
    val fileName: String? = null,
    val id: String? = null,
    val url: String? = null,
)

@Serializable public data class Announcement(
    val attachments: List<Attachment>? = null,
    val content: String? = null,
    val id: String? = null,
    val isEdited: Boolean? = null,
    val startDate: String? = null,
    val stopDate: String? = null,
    val title: String? = null,
)

@Serializable public data class DiaryAttachment(
    val id: String? = null,
    val previewUrl: String? = null,
    val thumbnailUrl: String? = null,
)

@Serializable public data class DiaryPost(
    val attachments: List<DiaryAttachment>? = null,
    val content: String? = null,
    val createdDate: String? = null,
    val id: String? = null,
    val title: String? = null,
)

@Serializable public data class Allergy(
    val allergy: Int? = null,
    val id: String? = null,
    val information: String? = null,
    val name: String? = null,
    val severity: Int? = null,
)

@Serializable public data class FoodReservation(
    val foodReservation: Int? = null,
    val id: String? = null,
    val information: String? = null,
    val name: String? = null,
)

@Serializable public data class HealthCondition(
    val id: String? = null,
    val information: String? = null,
    val name: String? = null,
)

@Serializable public data class Medication(
    val id: String? = null,
    val information: String? = null,
    val medicineName: String? = null,
)

@Serializable public data class GeneralPractitioner(
    val address: String? = null,
    val addressPostCode: String? = null,
    val city: String? = null,
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
)

@Serializable public data class HealthInformation(
    val allergies: List<Allergy>? = null,
    val foodReservations: List<FoodReservation>? = null,
    val generalPractitioners: List<GeneralPractitioner>? = null,
    val healthConditions: List<HealthCondition>? = null,
    val hideHealthConditions: Boolean? = null,
    val medications: List<Medication>? = null,
)

@Serializable public data class SleepLog(
    val from: String? = null,
    val id: String? = null,
    val to: String? = null,
)

@Serializable public data class MessageSender(
    val firstName: String? = null,
    val lastName: String? = null,
    val personId: String? = null,
    val senderType: Int? = null,
)

@Serializable public data class Message(
    val createdDate: String? = null,
    val id: String? = null,
    val isSeen: Boolean? = null,
    val message: String? = null,
    val sender: MessageSender? = null,
)

@Serializable public data class UnseenMessagesCount(
    val totalCount: Int? = null,
)

@Serializable public data class KindergartenInformation(
    val address: String? = null,
    val city: String? = null,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val postCode: String? = null,
    val units: List<UnitInformation>? = null,
)

@Serializable public data class UnitInformation(
    val email: String? = null,
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
)

@Serializable public data class StatusComponent(
    val id: String? = null,
    val status: String? = null,
)

@Serializable public data class ComponentsResponse(
    val components: List<StatusComponent>? = null,
)

@Serializable public data class Incident(
    val components: List<StatusComponent>? = null,
    val id: String? = null,
    @SerialName("scheduled_for") val scheduledFor: String? = null,
    @SerialName("scheduled_until") val scheduledUntil: String? = null,
    val status: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
)

@Serializable public data class IncidentsResponse(
    val incidents: List<Incident>? = null,
)

@Serializable public data class MaintenancesResponse(
    @SerialName("scheduled_maintenances") val maintenances: List<Incident>? = null,
)
