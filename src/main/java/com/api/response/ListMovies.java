package com.api.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class ListMovies {
    public final String status;
    public final String status_message;
    public final Data data;
    public final Meta meta;

    @JsonCreator
    public ListMovies(@JsonProperty("status") String status, 
    		@JsonProperty("status_message") String status_message, 
    		@JsonProperty("data") Data data, @JsonProperty("@meta") Meta meta){
        this.status = status;
        this.status_message = status_message;
        this.data = data;
        this.meta = meta;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class Data {
        public final long movie_count;
        public final long limit;
        public final long page_number;
        public final Movy[] movies;

        @JsonCreator
        public Data(@JsonProperty("movie_count") long movie_count, @JsonProperty("limit") long limit, @JsonProperty("page_number") long page_number, @JsonProperty("movies") Movy[] movies){
            this.movie_count = movie_count;
            this.limit = limit;
            this.page_number = page_number;
            this.movies = movies;
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class Movy {
            public final long id;
            public final String url;
            public final String imdb_code;
            public final String title;
            public final String title_english;
            public final String title_long;
            public final String slug;
            public final long year;
            public final double rating;
            public final long runtime;
            public final String[] genres;
            public final String summary;
            public final String description_full;
            public final String synopsis;
            public final String yt_trailer_code;
            public final String language;
            public final String mpa_rating;
            public final String background_image;
            public final String background_image_original;
            public final String small_cover_image;
            public final String medium_cover_image;
            public final String large_cover_image;
            public final String state;
            public final Torrent[] torrents;
            public final String date_uploaded;
            public final long date_uploaded_unix;
    
            @JsonCreator
            public Movy(@JsonProperty("id") long id, @JsonProperty("url") String url, @JsonProperty("imdb_code") String imdb_code, @JsonProperty("title") String title, @JsonProperty("title_english") String title_english, @JsonProperty("title_long") String title_long, @JsonProperty("slug") String slug, @JsonProperty("year") long year, @JsonProperty("rating") double rating, @JsonProperty("runtime") long runtime, @JsonProperty("genres") String[] genres, @JsonProperty("summary") String summary, @JsonProperty("description_full") String description_full, @JsonProperty("synopsis") String synopsis, @JsonProperty("yt_trailer_code") String yt_trailer_code, @JsonProperty("language") String language, @JsonProperty("mpa_rating") String mpa_rating, @JsonProperty("background_image") String background_image, @JsonProperty("background_image_original") String background_image_original, @JsonProperty("small_cover_image") String small_cover_image, @JsonProperty("medium_cover_image") String medium_cover_image, @JsonProperty("large_cover_image") String large_cover_image, @JsonProperty("state") String state, @JsonProperty("torrents") Torrent[] torrents, @JsonProperty("date_uploaded") String date_uploaded, @JsonProperty("date_uploaded_unix") long date_uploaded_unix){
                this.id = id;
                this.url = url;
                this.imdb_code = imdb_code;
                this.title = title;
                this.title_english = title_english;
                this.title_long = title_long;
                this.slug = slug;
                this.year = year;
                this.rating = rating;
                this.runtime = runtime;
                this.genres = genres;
                this.summary = summary;
                this.description_full = description_full;
                this.synopsis = synopsis;
                this.yt_trailer_code = yt_trailer_code;
                this.language = language;
                this.mpa_rating = mpa_rating;
                this.background_image = background_image;
                this.background_image_original = background_image_original;
                this.small_cover_image = small_cover_image;
                this.medium_cover_image = medium_cover_image;
                this.large_cover_image = large_cover_image;
                this.state = state;
                this.torrents = torrents;
                this.date_uploaded = date_uploaded;
                this.date_uploaded_unix = date_uploaded_unix;
            }

            @JsonIgnoreProperties(ignoreUnknown=true)
            public static final class Torrent {
                public final String url;
                public final String hash;
                public final String quality;
                public final long seeds;
                public final long peers;
                public final String size;
                public final long size_bytes;
                public final String date_uploaded;
                public final long date_uploaded_unix;
        
                @JsonCreator
                public Torrent(@JsonProperty("url") String url, @JsonProperty("hash") String hash, @JsonProperty("quality") String quality, @JsonProperty("seeds") long seeds, @JsonProperty("peers") long peers, @JsonProperty("size") String size, @JsonProperty("size_bytes") long size_bytes, @JsonProperty("date_uploaded") String date_uploaded, @JsonProperty("date_uploaded_unix") long date_uploaded_unix){
                    this.url = url;
                    this.hash = hash;
                    this.quality = quality;
                    this.seeds = seeds;
                    this.peers = peers;
                    this.size = size;
                    this.size_bytes = size_bytes;
                    this.date_uploaded = date_uploaded;
                    this.date_uploaded_unix = date_uploaded_unix;
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class Meta {
        public final long server_time;
        public final String server_timezone;
        public final long api_version;
        public final String execution_time;

        @JsonCreator
        public Meta(@JsonProperty("server_time") long server_time, @JsonProperty("server_timezone") String server_timezone, @JsonProperty("api_version") long api_version, @JsonProperty("execution_time") String execution_time){
            this.server_time = server_time;
            this.server_timezone = server_timezone;
            this.api_version = api_version;
            this.execution_time = execution_time;
        }
    }

    public List<String> getMovieIds() {
    		
	    	List<String> movieIds = new ArrayList<String>();
	    	
	    	for(Data.Movy movie : data.movies) {
	    		movieIds.add(String.valueOf(movie.id));
	    	}
    	
	    	return movieIds;
    }
    
    public List<String> getMovieNames() {
		
    	List<String> movieNames = new ArrayList<String>();
    	
    	for(Data.Movy movie : data.movies) {
    		movieNames.add(String.valueOf(movie.title));
    	}
	
    	return movieNames;
}
}