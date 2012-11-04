package me.enkode.rdio

import java.io.InputStreamReader
import org.apache.commons.io.{FileUtils, IOUtils}

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
		val API_KEY = IOUtils.toString(getClass.getResourceAsStream("/api.key")).trim
		val API_SECRET = IOUtils.toString(getClass.getResourceAsStream("/api.secret")).trim

		val rdio = new RdioAPI(API_SECRET, API_KEY, getPIN)

		println(rdio.getPlaylists.owned.map(_.name).mkString("\n"))
	}
}
