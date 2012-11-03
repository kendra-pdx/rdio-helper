package me.enkode.rdio

import org.scribe.builder.ServiceBuilder
import org.scribe.model.{Response, Verifier, Verb, OAuthRequest}

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

	private def callMethod(method: String): Response = {
		val request = new OAuthRequest(Verb.POST, "http://api.rdio.com/1/") {
			addBodyParameter("method", method)
		}
		service.signRequest(accessToken, request)
		request.send()
	}

	def getPlaylists = {
		callMethod("getPlaylists").getBody
	}
}
