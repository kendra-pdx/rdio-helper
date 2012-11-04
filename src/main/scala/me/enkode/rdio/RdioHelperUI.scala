package me.enkode.rdio

import model.Playlist
import scala.swing._
import event.ButtonClicked
import org.apache.commons.io.IOUtils
import actors.Futures

object RdioHelperUI extends SimpleSwingApplication {
	val API_KEY = IOUtils.toString(getClass.getResourceAsStream("/api.key")).trim
	val API_SECRET = IOUtils.toString(getClass.getResourceAsStream("/api.secret")).trim

	val rdio = new RdioAPI(API_SECRET, API_KEY, (url: String) => {
		//todo: only works on macos
		Runtime.getRuntime.exec(Array("open", url))
		Dialog.showInput[String](message = "PIN?", initial = "").getOrElse(throw new RuntimeException("no pin"))
	})

	val sourceListView = new ListView[Playlist]()
	val destListView = new ListView[Playlist]()

	Futures.future {
		val playlists = rdio.getPlaylists.owned
		sourceListView.listData = playlists
		destListView.listData = playlists
	}

	val top = new MainFrame {
		title = "rdio helper"
		contents = new BoxPanel(Orientation.Vertical) {
			contents += sourceListView
			contents += new BoxPanel(Orientation.Horizontal) {
				contents += new Button("copy to playlists") {
					reactions += {
						case ButtonClicked(button) => {
							println("copy to other playlists clicked")
							val trackKeysToCopy: Set[String] = sourceListView.selection.items.map(_.trackKeys).flatten.toSet
							println(trackKeysToCopy)
							button.enabled = false
							Futures.future {
								destListView.selection.items.map { destPlaylist =>
									rdio.addToPlaylist(destPlaylist.key, trackKeysToCopy -- destPlaylist.trackKeys)
								}
								button.enabled = true
							}
						}
					}
				}
				contents += new Button("copy to collection") {
					enabled = false
					reactions += {
						case ButtonClicked(_) => {
							println("copy to collection clicked")
						}
					}
				}
			}
			contents += destListView
		}
	}
}
