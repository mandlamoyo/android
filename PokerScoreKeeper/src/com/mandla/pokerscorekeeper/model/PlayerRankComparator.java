package com.mandla.pokerscorekeeper.model;

import java.util.Comparator;

public class PlayerRankComparator implements Comparator<Player> {

	@Override
	public int compare(Player p1, Player p2 ) {
		int rank1 = Integer.parseInt( p1.getAttribute( Player.RANK ));
		int rank2 = Integer.parseInt( p2.getAttribute( Player.RANK ));
		
		if( rank1 > rank2 ) {
			return 1;
		} else if( rank1 < rank2 ) {
			return -1;
		} else {
			return 0;
		}
	}
}
