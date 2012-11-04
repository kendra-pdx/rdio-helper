package me.enkode.rdio

import model.{GetPlaylistsResponse, Playlist}
import org.scribe.builder.ServiceBuilder
import org.scribe.model.{Response, Verifier, Verb, OAuthRequest}
import com.codahale.jerkson.Json
import swing.Dialog

class RdioAPI(secret: String, key: String, getPin: (String) => String) {
	val service = new ServiceBuilder()
		.provider(classOf[RdioProvider])
		.apiKey(key)
		.apiSecret(secret)
		.debug()
		.build()

	val requestToken = service.getRequestToken

	val verifier = new Verifier(
		getPin(
			service.getAuthorizationUrl(requestToken)
		)
	)

	val accessToken = service.getAccessToken(requestToken, verifier)

	private def callMethod(method: String, parameters: Map[String, String] = Map.empty): Response = {
		val request = new OAuthRequest(Verb.POST, "http://api.rdio.com/1/") {
			addBodyParameter("method", method)
			parameters.foreach {
				case (k, v) => addBodyParameter(k, v)
			}
		}
		service.signRequest(accessToken, request)
		request.send()
	}

	def getPlaylists = {
		val body = callMethod("getPlaylists", Map(
			"extras" -> "trackKeys"
		)).getBody
		println(body)
		Json.parse[GetPlaylistsResponse](body).result
	}
}
