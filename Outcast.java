
public class Outcast {
    private WordNet net;
    
    public Outcast(WordNet wordNet){
	net = wordNet;
    }

    public String outcast(String[] nouns){
	int[] d = new int[nouns.length];
	int di;
	for(int i = 0; i<d.length;i++){
	    di = 0;
	    for(int j = 0; j<d.length;j++){
		if(j==i) j++;
		di += net.distance(nouns[i], nouns[j]);
	    }
	    d[i] = di;
	}
	
	int maxDistance = d[0], maxId = 0;
	for(int i = 0; i<d.length;i++){
	    if(d[i]>maxDistance){
		maxId = i;
		maxDistance = d[i];
	    }
	}
	return nouns[maxId];
    }
}
