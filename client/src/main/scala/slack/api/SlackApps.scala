package slack.api

import slack.SlackError
import slack.core.access.{ clientId, clientSecret, ClientSecret }
import zio.ZIO

object SlackApps {
  trait Service {
    def uninstall: ZIO[slack.SlackEnv with ClientSecret, SlackError, Boolean] =
      for {
        (id, secret) <- clientId <&> clientSecret
        ok           <- sendM(request("apps.uninstall", "client_id" -> id, "client_secret" -> secret)) >>= isOk
      } yield ok
  }
}

object apps extends SlackApps.Service
