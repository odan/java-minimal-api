package com.odan.util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Handlebars.SafeString;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.google.inject.Inject;
import io.javalin.rendering.FileRenderer;
import java.io.IOException;
import java.util.Map;

public class HandlebarsRenderer implements FileRenderer
{

    private static final String TEMPLATE_SUFFIX = ".hbs";

    private final Handlebars handlebars;

    @Inject
    public HandlebarsRenderer(Handlebars handlebars)
    {
        this.handlebars = handlebars;
        this.handlebars.registerHelper("block", this::renderBlock);
        this.handlebars.registerHelper("partial", this::capturePartial);
    }

    @Override
    public String render(String filePath, Map<String, ? extends Object> model, io.javalin.http.Context context)
    {
        try {
            Template template = handlebars.compile(normalize(filePath));
            return template.apply(model).trim();
        } catch (IOException e) {
            throw new IllegalStateException("Could not render template: " + filePath, e);
        }
    }

    private CharSequence renderBlock(Object name, Options options) throws IOException
    {
        Object value = options.data(String.valueOf(name));
        if (value == null) {
            return new SafeString(options.fn().toString());
        }

        return new SafeString(value.toString());
    }

    private CharSequence capturePartial(Object name, Options options) throws IOException
    {
        options.data(String.valueOf(name), options.fn());
        return "";
    }

    private String normalize(String filePath)
    {
        if (filePath.endsWith(TEMPLATE_SUFFIX)) {
            return filePath.substring(0, filePath.length() - TEMPLATE_SUFFIX.length());
        }

        return filePath;
    }
}
