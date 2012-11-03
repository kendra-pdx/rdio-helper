package me.enkode.rdio

import org.scribe.builder.api.DefaultApi10a
import org.scribe.model.Token

class RdioProvider extends DefaultApi10a{
	def getRequestTokenEndpoint: String = "http://api.rdio.com/oauth/request_token"

	def getAccessTokenEndpoint: String = "http://api.rdio.com/oauth/access_token"

	def getAuthorizationUrl(token: Token): String = "https://www.rdio.com/oauth/authorize?oauth_token=%s".format(token.getToken)
}
