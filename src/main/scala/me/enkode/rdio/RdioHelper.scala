package me.enkode.rdio

import java.io.InputStreamReader

object RdioHelper {
	def getPIN(authUrl: String): String = {
		Runtime.getRuntime.exec(Array("open", authUrl))

		val reader = new InputStreamReader(System.in)
		val pinBuilder = new StringBuilder
		var c: Char = reader.read().toChar
		while (c != '\n') {
			pinBuilder.append(c)
			c = reader.read().toChar
		}
		pinBuilder.toString()
	}

	def main(args: Array[String]) {
		val API_KEY = "edhrkha5zz2qyhmsj246k96u"
		val API_SECRET = "UBcH3nFTmD"

		val rdio = new RdioAPI(API_SECRET, API_KEY, getPIN)

		println(rdio.getPlaylists)
	}
}
