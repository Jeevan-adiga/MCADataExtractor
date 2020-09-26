package com.api.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class MoviesDetails {
    public final String status;
    public final String status_message;
    public final Data data;
    public final Meta meta;

    @JsonCreator
    public MoviesDetails(@JsonProperty("status") String status, 
    		@JsonProperty("status_message") String status_message, 
    		@JsonProperty("data") Data data, 
    		@JsonProperty("@meta") Meta meta){
        this.status = status;
        this.status_message = status_message;
        this.data = data;
        this.meta = meta;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static final class Data {
        public final Movie movie;

        @JsonCreator
        public Data(@JsonProperty("movie") Movie movie){
            this.movie = movie;
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        public static final class Movie {
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
            public final long download_count;
            public final long like_count;
            public final String description_intro;
            public final String description_full;
            public final String yt_trailer_code;
            public final String language;
            public final String mpa_rating;
            public final String background_image;
            public final String background_image_original;
            public final String small_cover_image;
            public final String medium_cover_image;
            public final String large_cover_image;
            public final String medium_screenshot_image1;
            public final String medium_screenshot_image2;
            public final String medium_screenshot_image3;
            public final String large_screenshot_image1;
            public final String large_screenshot_image2;
            public final String large_screenshot_image3;
            public final Cast[] cast;
            public final Torrent[] torrents;
            public final String date_uploaded;
            public final long date_uploaded_unix;
    
            @JsonCreator
            public Movie(@JsonProperty("id") long id, @JsonProperty("url") String url, @JsonProperty("imdb_code") String imdb_code, @JsonProperty("title") String title, @JsonProperty("title_english") String title_english, @JsonProperty("title_long") String title_long, @JsonProperty("slug") String slug, @JsonProperty("year") long year, @JsonProperty("rating") double rating, @JsonProperty("runtime") long runtime, @JsonProperty("genres") String[] genres, @JsonProperty("download_count") long download_count, @JsonProperty("like_count") long like_count, @JsonProperty("description_intro") String description_intro, @JsonProperty("description_full") String description_full, @JsonProperty("yt_trailer_code") String yt_trailer_code, @JsonProperty("language") String language, @JsonProperty("mpa_rating") String mpa_rating, @JsonProperty("background_image") String background_image, @JsonProperty("background_image_original") String background_image_original, @JsonProperty("small_cover_image") String small_cover_image, @JsonProperty("medium_cover_image") String medium_cover_image, @JsonProperty("large_cover_image") String large_cover_image, @JsonProperty("medium_screenshot_image1") String medium_screenshot_image1, @JsonProperty("medium_screenshot_image2") String medium_screenshot_image2, @JsonProperty("medium_screenshot_image3") String medium_screenshot_image3, @JsonProperty("large_screenshot_image1") String large_screenshot_image1, @JsonProperty("large_screenshot_image2") String large_screenshot_image2, @JsonProperty("large_screenshot_image3") String large_screenshot_image3, @JsonProperty("cast") Cast[] cast, @JsonProperty("torrents") Torrent[] torrents, @JsonProperty("date_uploaded") String date_uploaded, @JsonProperty("date_uploaded_unix") long date_uploaded_unix){
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
                this.download_count = download_count;
                this.like_count = like_count;
                this.description_intro = description_intro;
                this.description_full = description_full;
                this.yt_trailer_code = yt_trailer_code;
                this.language = language;
                this.mpa_rating = mpa_rating;
                this.background_image = background_image;
                this.background_image_original = background_image_original;
                this.small_cover_image = small_cover_image;
                this.medium_cover_image = medium_cover_image;
                this.large_cover_image = large_cover_image;
                this.medium_screenshot_image1 = medium_screenshot_image1;
                this.medium_screenshot_image2 = medium_screenshot_image2;
                this.medium_screenshot_image3 = medium_screenshot_image3;
                this.large_screenshot_image1 = large_screenshot_image1;
                this.large_screenshot_image2 = large_screenshot_image2;
                this.large_screenshot_image3 = large_screenshot_image3;
                this.cast = cast;
                this.torrents = torrents;
                this.date_uploaded = date_uploaded;
                this.date_uploaded_unix = date_uploaded_unix;
            }

            @JsonIgnoreProperties(ignoreUnknown=true)
            public static final class Cast {
                public final String name;
                public final String character_name;
                public final String url_small_image;
                public final String imdb_code;
        
                @JsonCreator
                public Cast(@JsonProperty("name") String name, @JsonProperty("character_name") String character_name, @JsonProperty("url_small_image") String url_small_image, @JsonProperty("imdb_code") String imdb_code){
                    this.name = name;
                    this.character_name = character_name;
                    this.url_small_image = url_small_image;
                    this.imdb_code = imdb_code;
                }
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
        public Meta(@JsonProperty("server_time") long server_time, 
        		@JsonProperty("server_timezone") String server_timezone, 
        		@JsonProperty("api_version") long api_version, 
        		@JsonProperty("execution_time") String execution_time){
            this.server_time = server_time;
            this.server_timezone = server_timezone;
            this.api_version = api_version;
            this.execution_time = execution_time;
        }
    }
    
	public List<String> returnCastDetails() {

		List<String> castDetails = new ArrayList<String>();

		if(data.movie.cast != null) {
            for (Data.Movie.Cast cast : data.movie.cast) {
                castDetails.add(cast.name + " as " + cast.character_name);
            }
        }
		
		return castDetails;
	}
}