import java.util.ArrayList;
import java.util.Collections; //imported to use shuffle method in colletions
import java.util.Random; //random number class used for shuffle

/**
 * A class to hold details of audio tracks.
 * Individual tracks may be played. 
 * 
 * A new playRandomTrack() method has been added.
 * 
 * @author Matthew Sheehan
 * @version 2020.02.22
 */
public class MusicOrganizer
{
    // An ArrayList for storing music tracks.
    private ArrayList<Track> tracks;
    private ArrayList<Track> shuffledTracks;
    // A player for the music tracks.
    private MusicPlayer player;
    // A reader that can read music files and load them as tracks.
    private TrackReader reader;
    // A random number generator for shuffle function
    private Random rng;
    //marker for last played track so shuffle doesnt repeat same song
    private int lastPlayedTrack;
    //marker for position on shuffled play list
    private int shufflePosition = 0;
    
    

    /**
     * Create a MusicOrganizer
     */
    public MusicOrganizer()
    {
        tracks = new ArrayList<>();
        player = new MusicPlayer();
        reader = new TrackReader();
        shuffledTracks = new ArrayList<>();
        
        readLibrary("../audio");
        //clones tracks<> to shuffledTracks<> and shuffles
        cloneAndShuffle();
        
        System.out.println("Music library loaded. " + getNumberOfTracks() + " tracks.");
        System.out.println();
    }
   
    
    /**
     * Add a track file to the collection.
     * @param filename The file name of the track to be added.
     */
    public void addFile(String filename)
    {
        tracks.add(new Track(filename));
        //update Shuffle list and reshuffle so new tracks 
        //   arent just tacked on at end.
        cloneAndShuffle();
    }
    
    /**
     * Add a track to the collection.
     * @param track The track to be added.
     */
    public void addTrack(Track track)
    {
        tracks.add(track);
        //update Shuffle list and reshuffle so new tracks 
        //   arent just tacked on at end.
        cloneAndShuffle();        
    }
    
    /**
     * Play a track in the collection.
     * @param index The index of the track to be played.
     */
    public void playTrack(int index)
    {
        if(indexValid(index)) {
            Track track = tracks.get(index);
            player.startPlaying(track.getFilename());
            System.out.println("Now playing: " + track.getArtist() + " - " + track.getTitle());
            lastPlayedTrack = index;
        }
     }
    
    /**
     * Return the number of tracks in the collection.
     * @return The number of tracks in the collection.
     */
    public int getNumberOfTracks()
    {
        return tracks.size();
    }
    
    /**
     * List a track from the collection.
     * @param index The index of the track to be listed.
     */
    public void listTrack(int index)
    {
        System.out.print("Track " + index + ": ");
        Track track = tracks.get(index);
        System.out.println(track.getDetails());
    }
    
    /**
     * Show a list of all the tracks in the collection.
     */
    public void listAllTracks()
    {
        System.out.println("Track listing: ");

        for(Track track : tracks) {
            System.out.println(track.getDetails());
        }
        System.out.println();
    }
    

    /**
     * List all tracks by the given artist.
     * @param artist The artist's name.
     */
    public void listByArtist(String artist)
    {
        for(Track track : tracks) {
            if(track.getArtist().contains(artist)) {
                System.out.println(track.getDetails());
            }
        }
    }
    
    /**
     * Remove a track from the collection.
     * @param index The index of the track to be removed.
     */
    public void removeTrack(int index)
    {
        if(indexValid(index)) {
            tracks.remove(index);
        //update Shuffle list and reshuffle.
            cloneAndShuffle();
        }
    }
    
    /**
     * Play the first track in the collection, if there is one.
     */
    public void playFirst()
    {
        if(tracks.size() > 0) {
            player.startPlaying(tracks.get(0).getFilename());
            lastPlayedTrack = 0;
        }
    }
    
    /**
     * Stop the player.
     */
    public void stopPlaying()
    {
        player.stop();
    }

    /**
     * Determine whether the given index is valid for the collection.
     * Print an error message if it is not.
     * @param index The index to be checked.
     * @return true if the index is valid, false otherwise.
     */
    private boolean indexValid(int index)
    {
        // The return value.
        // Set according to whether the index is valid or not.
        boolean valid;
        
        if(index < 0) {
            System.out.println("Index cannot be negative: " + index);
            valid = false;
        }
        else if(index >= tracks.size()) {
            System.out.println("Index is too large: " + index);
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }
    
    private void readLibrary(String folderName)
    {
        ArrayList<Track> tempTracks = reader.readTracks(folderName, ".mp3");

        // Put all thetracks into the organizer.
        for(Track track : tempTracks) {
            addTrack(track);
        }

    }
    
    /**
     *4.43
     */
    
    /**
     *This method plays one random track if there is more than 2 tracks
     */
    public void playOneRandomTrack()
    {   
        player.stop();
        Random rng = new Random();
        int random = rng.nextInt(tracks.size());

            if(tracks.size()<2)
            { //if there's not enough tracks to randomize, do nothing.
                System.out.println("Not enough tracks to play a random track.");        
            }else if(random != lastPlayedTrack 
                && tracks.size()>1) //the && clause here should be redundant
                {//if wasn't played last and there are more than one track
                    player.startPlaying(tracks.get(random).getFilename());
                    System.out.println(tracks.get(random).getDetails());
                    lastPlayedTrack = random;
                
            }else{    // the track last played = the random number.
                while(random == lastPlayedTrack)
                    { //shuffle until unique
                        random = rng.nextInt(getNumberOfTracks());
                            if(random!= lastPlayedTrack) break;
                    }
                    player.startPlaying(tracks.get(random).getFilename());
                    System.out.println(tracks.get(random).getDetails());
                    lastPlayedTrack = random;
            }
     }
    
    /**
     * 4.45
     **/
    /**
     * clone tracks a dedicated shuffle arrray to reshuffle songs
     * used to update when added new songs.
     */
    public void cloneAndShuffle(){
        shuffledTracks = (ArrayList)tracks.clone();
        Collections.shuffle(shuffledTracks);
        shufflePosition = 0; //reset place on shuffle list
    } 
    
    /** 
     * Show a list of all the shuffled tracks as they are currently ordered.
     * adding/Removing new tracks will reshuffle
     */
    public void listAllShuffledTracks()
    {
        System.out.println("Track listing: ");

        for(Track track : shuffledTracks) {
            System.out.println(track.getDetails());
        }
        System.out.println();
    }
    
    /**
     * This method plays from the shuffled list order one at a time.
     * I use collections.shuffle() method since I am playing one song once.
     */
    public void playFromShuffleList(){
        player.stop();
        if(shufflePosition == shuffledTracks.size()){ 
            //List is exhausted
                System.out.println("Shuffled playlist ended use method cloneAndShuffle to start again");
            }
            
        if(shuffledTracks.size() > 0 && shufflePosition < shuffledTracks.size()) 
        { // if list size is valid
            player.startPlaying(shuffledTracks.get(shufflePosition).getFilename());
            System.out.println("Now playing: " + shuffledTracks.get(shufflePosition).getArtist() + " - " + shuffledTracks.get(shufflePosition).getTitle());
            shufflePosition ++; 
        }
    }

}
