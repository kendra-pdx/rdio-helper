package me.enkode.rdio.model

case class Playlist(
	key: String,
	name: String,
	trackKeys: Set[String]
	) {
	override def toString: String = name
}

case class Playlists(
	subscribed: List[Playlist],
	collab: List[Playlist],
	owned: List[Playlist]
	)

case class GetPlaylistsResponse(
	status: String,
	result: Playlists
	)
