package slack

import slack.models.{Group, HistoryChunk}
import zio.ZIO

//@accessible
//@mockable
trait SlackGroups {
  val slackGroups: SlackGroups.Service[Any]
}

object SlackGroups {

  trait Service[R] {

    def archiveGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.archive", "channel" -> channelId)) >>= isOk

    def closeGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.close", "channel" -> channelId)) >>= isOk

    def createGroup(name: String): ZIO[R with SlackEnv, SlackError, Group] =
      sendM(request("groups.create", "name" -> name)) >>= as[Group]("group")

    def createChildGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Group] =
      sendM(request("groups.createChild", "channel" -> channelId)) >>= as[Group]("group")

    def getGroupHistory(channelId: String,
                        latest: Option[String] = None,
                        oldest: Option[String] = None,
                        inclusive: Option[Int] = None,
                        count: Option[Int] = None): ZIO[R with SlackEnv, SlackError, HistoryChunk] =
      sendM(
        request("groups.history",
                "channel" -> channelId,
                "latest" -> latest,
                "oldest" -> oldest,
                "inclusive" -> inclusive,
                "count" -> count)
      ) >>= as[HistoryChunk]

    def getGroupInfo(channelId: String): ZIO[R with SlackEnv, SlackError, Group] =
      sendM(request("groups.info", "channel" -> channelId)) >>= as[Group]("group")

    def inviteToGroup(channelId: String, userId: String): ZIO[R with SlackEnv, SlackError, Group] =
      sendM(request("groups.invite", "channel" -> channelId, "user" -> userId)) >>= as[Group]("group")

    def kickFromGroup(channelId: String, userId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.kick", "channel" -> channelId, "user" -> userId)) >>= isOk

    def leaveGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.leave", "channel" -> channelId)) >>= isOk

    def listGroups(excludeArchived: Int = 0): ZIO[R with SlackEnv, SlackError, Seq[Group]] =
      sendM(request("groups.list", "exclude_archived" -> excludeArchived.toString)) >>= as[Seq[Group]]("groups")

    def markGroup(channelId: String, ts: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.mark", "channel" -> channelId, "ts" -> ts)) >>= isOk

    def openGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.open", "channel" -> channelId)) >>= isOk

    // TODO: Lite Group Object
    def renameGroup(channelId: String, name: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.rename", "channel" -> channelId, "name" -> name)) >>= isOk

    def setGroupPurpose(channelId: String, purpose: String): ZIO[R with SlackEnv, SlackError, String] =
      sendM(request("groups.setPurpose", "channel" -> channelId, "purpose" -> purpose)) >>= as[String]("purpose")

    def setGroupTopic(channelId: String, topic: String): ZIO[R with SlackEnv, SlackError, String] =
      sendM(request("groups.setTopic", "channel" -> channelId, "topic" -> topic)) >>= as[String]("topic")

    def unarchiveGroup(channelId: String): ZIO[R with SlackEnv, SlackError, Boolean] =
      sendM(request("groups.unarchive", "channel" -> channelId)) >>= isOk
  }
}

object groups extends SlackGroups.Service[SlackEnv]