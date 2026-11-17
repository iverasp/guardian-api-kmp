package com.visma.guardian

import com.visma.guardian.model.Absence
import com.visma.guardian.model.AbsenceDetails
import com.visma.guardian.model.AbsenceSearchRequest
import com.visma.guardian.model.Announcement
import com.visma.guardian.model.AnnouncementSearchRequest
import com.visma.guardian.model.AttendanceLogResponse
import com.visma.guardian.model.CalendarCountsRequest
import com.visma.guardian.model.CalendarCountsResponse
import com.visma.guardian.model.CalendarEvent
import com.visma.guardian.model.CalendarEventDetails
import com.visma.guardian.model.CalendarEventRequest
import com.visma.guardian.model.CalendarEventsRequest
import com.visma.guardian.model.ChangeChildRequest
import com.visma.guardian.model.ChangeConsentRequest
import com.visma.guardian.model.ChildDetails
import com.visma.guardian.model.Consent
import com.visma.guardian.model.ContactPersonsResponse
import com.visma.guardian.model.CreateAbsenceRequest
import com.visma.guardian.model.CreateSessionRequest
import com.visma.guardian.model.CreateSessionResponse
import com.visma.guardian.model.CredentialStore
import com.visma.guardian.model.Credentials
import com.visma.guardian.model.DiaryPost
import com.visma.guardian.model.DiaryPostSearchRequest
import com.visma.guardian.model.HealthInformation
import com.visma.guardian.model.IdResponse
import com.visma.guardian.model.KindergartenInformation
import com.visma.guardian.model.ListResponse
import com.visma.guardian.model.MarkMessageSeenRequest
import com.visma.guardian.model.Message
import com.visma.guardian.model.Municipality
import com.visma.guardian.model.PaginationRequest
import com.visma.guardian.model.RegisterDeviceRequest
import com.visma.guardian.model.SendMessageRequest
import com.visma.guardian.model.SessionInfo
import com.visma.guardian.model.SleepLog
import com.visma.guardian.model.UnitInformation
import com.visma.guardian.model.UnseenMessagesCount
import com.visma.guardian.model.UpdateAbsenceRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import io.ktor.http.isSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class GuardianApi(
    private val httpClient: HttpClient,
    private val credentialStore: CredentialStore,
    private val authClient: AuthClient,
    private val config: GuardianConfig = GuardianConfig(),
    public var languageCode: String = "nb",
) {
    private val refreshMutex = Mutex()

    public suspend fun municipalities(): List<Municipality> = publicGet("v2/customers")

    public suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse =
        post("v2/session", request, sessionRequired = false)

    public suspend fun session(): SessionInfo = get("v2/session")

    public suspend fun deleteSession() {
        request<Unit>(HttpMethod.Delete, "v1/session")
    }

    public suspend fun changeChild(id: String) {
        putUnit("v1/user/changechild", ChangeChildRequest(id))
    }

    public suspend fun childDetails(): ChildDetails = get("v1/children/details")

    public suspend fun contactPersons(): ContactPersonsResponse = get("v1/guardians")

    public suspend fun healthInformation(): HealthInformation = get("v1/children/health")

    public suspend fun attendanceLog(request: PaginationRequest): AttendanceLogResponse = post("v1/children/attendances/search", request)

    public suspend fun absences(request: AbsenceSearchRequest): ListResponse<AbsenceDetails> = post("v2/children/absences/search", request)

    public suspend fun absence(id: String): Absence = get("v2/children/absences/${id.pathSegment()}")

    public suspend fun createAbsence(request: CreateAbsenceRequest): IdResponse = post("v2/children/absences", request)

    public suspend fun updateAbsence(
        id: String,
        request: UpdateAbsenceRequest,
    ) {
        putUnit("v2/children/absences/${id.pathSegment()}", request)
    }

    public suspend fun deleteAbsence(id: String) {
        request<Unit>(HttpMethod.Delete, "v2/children/absences/${id.pathSegment()}")
    }

    public suspend fun announcements(request: AnnouncementSearchRequest): ListResponse<Announcement> =
        post("v1/announcements/search", request)

    public suspend fun announcement(id: String): Announcement = get("v1/announcements/${id.pathSegment()}")

    public suspend fun markAnnouncementSeen(id: String) {
        request<Unit>(HttpMethod.Put, "v1/announcements/${id.pathSegment()}/seen")
    }

    public suspend fun diaryPosts(request: DiaryPostSearchRequest): ListResponse<DiaryPost> =
        post("v1/children/diary-posts/search", request)

    public suspend fun diaryPost(id: String): DiaryPost = get("v1/children/diary-posts/${id.pathSegment()}")

    public suspend fun calendarEvents(request: CalendarEventsRequest): List<CalendarEvent> = post("v1/calendar/events/search", request)

    public suspend fun calendarCounts(request: CalendarCountsRequest): CalendarCountsResponse = post("v1/calendar/events/counts", request)

    public suspend fun calendarEvent(
        id: String,
        request: CalendarEventRequest,
    ): CalendarEventDetails = post("v2/calendar/events/${id.pathSegment()}", request)

    public suspend fun messages(
        fromMessageId: String,
        count: Int = 50,
        directionType: Int,
    ): List<Message> = get("v1/messages?FromMessageId=${fromMessageId.urlEncode()}&Count=$count&DirectionType=$directionType")

    public suspend fun unseenMessagesCount(): UnseenMessagesCount = get("v1/messages/unseen/count")

    public suspend fun sendMessage(message: String): IdResponse = post("v1/messages", SendMessageRequest(message))

    public suspend fun markMessageSeen(id: String) {
        postUnit("v1/messages/seen", MarkMessageSeenRequest(id))
    }

    public suspend fun consents(): List<Consent> = get("v1/children/consents")

    public suspend fun consent(id: String): Consent = get("v1/children/consents/${id.pathSegment()}")

    public suspend fun changeConsent(
        id: String,
        approved: Boolean,
    ) {
        postUnit("v1/children/consents/${id.pathSegment()}", ChangeConsentRequest(id, approved))
    }

    public suspend fun kindergartenInformation(): KindergartenInformation = get("v1/kindergarten/contact-information")

    public suspend fun unitInformation(id: String): UnitInformation = get("v1/kindergarten/units/${id.pathSegment()}")

    public suspend fun registerDevice(request: RegisterDeviceRequest) {
        postUnit("v1/notifications/register", request)
    }

    public suspend fun sleepLogs(request: PaginationRequest): List<SleepLog> = post("v1/sleeping-list/logs/search", request)

    public suspend fun establishSession(
        clientContext: String,
        deviceId: String,
    ): CreateSessionResponse {
        val result = createSession(CreateSessionRequest(clientContext, languageCode, deviceId))
        val sessionId = result.sessionId ?: throw GuardianException.Authentication("Guardian returned no session ID")
        val credentials = credentialStore.load() ?: throw GuardianException.Authentication("No stored credentials")
        credentialStore.save(credentials.copy(sessionId = sessionId))
        return result
    }

    private suspend inline fun <reified T> publicGet(path: String): T {
        val response = httpClient.get(config.guardianBaseUrl + path)
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        return response.body()
    }

    private suspend inline fun <reified T> get(
        path: String,
        sessionRequired: Boolean = true,
    ): T = request(HttpMethod.Get, path, sessionRequired = sessionRequired)

    private suspend inline fun <reified T, reified B> post(
        path: String,
        body: B,
        sessionRequired: Boolean = true,
    ): T = request(HttpMethod.Post, path, body, sessionRequired)

    private suspend inline fun <reified T, reified B> put(
        path: String,
        body: B,
        sessionRequired: Boolean = true,
    ): T = request(HttpMethod.Put, path, body, sessionRequired)

    private suspend inline fun <reified B> postUnit(
        path: String,
        body: B,
        sessionRequired: Boolean = true,
    ) {
        request<B, Unit>(HttpMethod.Post, path, body, sessionRequired)
    }

    private suspend inline fun <reified B> putUnit(
        path: String,
        body: B,
        sessionRequired: Boolean = true,
    ) {
        request<B, Unit>(HttpMethod.Put, path, body, sessionRequired)
    }

    private suspend inline fun <reified T> request(
        method: HttpMethod,
        path: String,
        sessionRequired: Boolean = true,
    ): T = request<Unit, T>(method, path, null, sessionRequired)

    private suspend inline fun <reified B, reified T> request(
        method: HttpMethod,
        path: String,
        body: B?,
        sessionRequired: Boolean,
    ): T {
        val credentials = credentialStore.load() ?: throw GuardianException.Authentication("No stored credentials")
        if (sessionRequired && credentials.sessionId == null) throw GuardianException.Authentication("No Guardian session")
        var response = send(method, path, body, credentials)
        if (response.status == HttpStatusCode.Unauthorized) {
            val refreshed = refreshMutex.withLock { authClient.refreshStored(credentials.token.accessToken) }
            response = send(method, path, body, refreshed)
        }
        if (!response.status.isSuccess()) throw GuardianException.Http(response.status.value, response.body())
        if (T::class == Unit::class) return Unit as T
        return response.body()
    }

    private suspend inline fun <reified B> send(
        method: HttpMethod,
        path: String,
        body: B?,
        credentials: Credentials,
    ) = httpClient.request(config.guardianBaseUrl + path) {
        this.method = method
        accept(ContentType.Application.Json)
        header("Authorization", "Bearer ${credentials.token.accessToken}")
        credentials.sessionId?.let { header("SessionId", it) }
        header("Accept-Language", languageCode)
        header("User-Agent", config.userAgent)
        if (body != null) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    private fun String.pathSegment(): String = urlEncode()

    private fun String.urlEncode(): String = encodeURLParameter()
}
