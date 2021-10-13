package com.iliagubarev.tsystems.javaschool.ittunes.backend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public final class Application implements Runnable {

    // NOTE: Application entry point.
    public static void main(String... arguments) {
        new Application().run();
    }

    // NOTE: All songs are stored here
    private final Map<String, Song> songs = new LinkedHashMap<>();

    @Override
    public void run() {

        // NOTE: Initial configuration (error-handling, CORS, e.t.c)
        configure();

        // NOTE: Prepares initial data when application starts
        prepareInitialData();

        get("/", (request, response) -> {
           return html("templates/index", new Object(), response);
        });

        // NOTE: Gets a list of all available songs as HTML
        get("/song-list", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();
            model.put("songs", songs);
            return html("templates/song-list", model, response);
        });

        // NOTE: Gets a list of all available songs as HTML (partial)
        get("/song-list-partial", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();
            model.put("songs", songs);
            return html("templates/song-list-partial", model, response);
        });

        // NOTE: Creates a new Song
        post("/api/songs/", (request, response) -> {
            final Song invoice = body(Song.class, request);
            final Song song = createSong(invoice);
            return json(song, response);
        });

        // NOTE: Logs a successful start of application
        LOG.info("Application started on http://localhost:4567");
    }

    // service method
    private Song createSong(final Song song) {
        song.generateId();
        songs.put(song.id, song);
        return song;
    }

    private void prepareInitialData() {
        final Song stayingAlive = new Song();
        stayingAlive.setTitle("Stayin' Alive");
        stayingAlive.setArtist("Bee Gees");
        stayingAlive.setAlbum("OST: Saturday Night Fever");
        stayingAlive.setGenre("Soul");
        stayingAlive.setYear(1977);
        stayingAlive.generateId();
        songs.put(stayingAlive.id, stayingAlive);
        final Song johnnyBGoode = new Song();
        johnnyBGoode.setTitle("Johnny B. Goode");
        johnnyBGoode.setArtist("Chuck Berry");
        johnnyBGoode.setAlbum("Single record");
        johnnyBGoode.setGenre("Rock'n'Roll");
        johnnyBGoode.setYear(1958);
        johnnyBGoode.generateId();
        songs.put(johnnyBGoode.id, johnnyBGoode);
    }

    // NOTE: Scheme of Song model
    private final static class Song {

        private String id;
        private String title;
        private String artist;
        private String album;
        private String genre;
        private int year;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void generateId() {
            this.id = UUID.randomUUID().toString();
        }
    }

    // NOTE: Technical and utility stuff below

    private void configure() {
        configureCors();
        configureErrorHandlers();
        configureObjectMapper();
    }

    private void configureCors() {
        options("/*", (request, response) -> json(new Object(), response));
        before("/*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });
    }

    private void configureErrorHandlers() {
        exception(Exception.class, (exception, request, response) -> {
            LOG.error("Requested operation terminated with an error", exception);
            response.status(400);
            response.body(exception.getMessage());
        });
    }

    private void configureObjectMapper() {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        OBJECT_MAPPER.findAndRegisterModules();
    }

    private <T> T body(final Class<T> type, final Request request) {
        final String requestBody = request.body();
        if (requestBody == null || requestBody.isEmpty()) {
            throw new RuntimeException("Request body is missing");
        }
        final T result;
        try {
            result = OBJECT_MAPPER.readValue(request.body(), type);
        } catch (final IOException exception) {
            throw new RuntimeException("Request body format is invalid", exception);
        }
        return result;
    }

    private String json(final Object data, final Response response) {
        response.header("Content-Type", "application/json");
        try {
            return OBJECT_MAPPER.writeValueAsString(data != null ? data : new Object());
        } catch (final IOException exception) {
            throw new RuntimeException("JSON serialization has failed", exception);
        }
    }

    private String html(final String templatePath, final Object model, final Response response) {
        try {
            final String result = HANDLEBARS.compile(templatePath).apply(model);
            response.header("Content-Type", "text/html;charset=utf-8");
            return result;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final Handlebars HANDLEBARS = new Handlebars(new ClassPathTemplateLoader());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
