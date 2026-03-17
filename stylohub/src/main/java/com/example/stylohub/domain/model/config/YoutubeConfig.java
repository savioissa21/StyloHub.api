package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeConfig implements WidgetConfig {

    private final String videoId;
    private final boolean autoPlay;
    private final boolean showControls;

    @JsonCreator
    public YoutubeConfig(
            @JsonProperty("videoId") String videoId,
            @JsonProperty("autoPlay") boolean autoPlay,
            @JsonProperty("showControls") boolean showControls) {
        this.videoId = videoId;
        this.autoPlay = autoPlay;
        this.showControls = showControls;
        this.validate();
    }

    public String getVideoId() { return videoId; }
    public boolean isAutoPlay() { return autoPlay; }
    public boolean isShowControls() { return showControls; }

    @Override
    public WidgetType getType() {
        return WidgetType.YOUTUBE;
    }

    @Override
    public void validate() {
        if (videoId == null || videoId.isBlank()) {
            throw new DomainValidationException("O ID do vídeo do YouTube não pode estar vazio.");
        }
        if (videoId.length() != 11) {
            throw new DomainValidationException("O ID do vídeo do YouTube deve ter exatamente 11 caracteres.");
        }
    }
}
