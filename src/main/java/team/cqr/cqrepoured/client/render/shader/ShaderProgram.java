package team.cqr.cqrepoured.client.render.shader;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_SHADER_SOURCE_LENGTH;
import static org.lwjgl.opengl.GL20.GL_SHADER_TYPE;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class ShaderProgram {

	private static final Logger LOGGER = LogManager.getLogger();
	private final int shaderProgram;

	private ShaderProgram(Int2ObjectMap<Supplier<String>> shaderSourceProviders) throws ShaderException {
		Int2ObjectMap<String> shaderSources = new Int2ObjectLinkedOpenHashMap<>();
		for (Int2ObjectMap.Entry<Supplier<String>> entry : shaderSourceProviders.int2ObjectEntrySet()) {
			int type = entry.getIntKey();
			String source;
			try {
				source = entry.getValue().get();
			} catch (Exception e) {
				throw new ShaderException("Failed obtaining source for type " + typeName(type), e);
			}
			if (source == null) {
				throw new ShaderException("Shader source provider for type " + typeName(type) + " returned null");
			}
			shaderSources.put(type, source);
		}

		IntList shaders = new IntArrayList();
		try {
			for (Int2ObjectMap.Entry<String> entry : shaderSources.int2ObjectEntrySet()) {
				shaders.add(compileShader(entry.getIntKey(), entry.getValue()));
			}

			this.shaderProgram = linkProgram(shaders);
		} finally {
			shaders.forEach(GL20::glDeleteShader);
		}
	}

	private static int compileShader(int type, String source) throws ShaderException {
		int shader = GL20.glCreateShader(type);
		if (shader == 0) {
			throw new ShaderException("Failed creating shader with type " + typeName(type));
		}

		boolean compiled = false;
		try {
			GL20.glShaderSource(shader, source);
			GL20.glCompileShader(shader);

			compiled = GL20.glGetShaderi(shader, GL_COMPILE_STATUS) == GL_TRUE;
			Optional<String> shaderLog = getShaderLog(shader);
			if (!compiled) {
				String message = buildShaderCompileMessage("Failed compiling shader", type, source, shaderLog);
				LOGGER.error(message);
				throw new ShaderException(message);
			} else if (shaderLog.isPresent()) {
				LOGGER.info(buildShaderCompileMessage("Compiled shader", type, source, shaderLog));
			}
		} finally {
			if (!compiled) {
				GL20.glDeleteShader(shader);
			}
		}

		return shader;
	}

	private static Optional<String> getShaderLog(int shader) {
		return Optional.ofNullable(StringUtils.stripToNull(GL20.glGetShaderInfoLog(shader, GL20.glGetShaderi(shader, GL_INFO_LOG_LENGTH))));
	}

	private static String buildShaderCompileMessage(String message, int type, String source, Optional<String> shaderLog) {
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		sb.append("\n");
		sb.append("Type: ").append(typeName(type));
		sb.append("\n");
		sb.append("Source:").append("\n").append(source);
		if (shaderLog.isPresent()) {
			sb.append("\n");
			sb.append("Log:").append("\n").append(shaderLog.get());
		}
		return sb.toString();
	}

	private static int linkProgram(IntList shaders) throws ShaderException {
		int program = GL20.glCreateProgram();
		if (program == 0) {
			throw new ShaderException("Failed creating program");
		}

		boolean linked = false;
		try {
			for (int shader : shaders) {
				GL20.glAttachShader(program, shader);
			}
			GL20.glLinkProgram(program);
			for (int shader : shaders) {
				GL20.glDetachShader(program, shader);
			}

			linked = GL20.glGetProgrami(program, GL_LINK_STATUS) == GL_TRUE;
			Optional<String> programLog = getProgramLog(program);
			if (!linked) {
				String message = buildProgramLinkMessage("Failed linking program", shaders, programLog);
				LOGGER.error(message);
				throw new ShaderException(message);
			} else if (programLog.isPresent()) {
				LOGGER.info(buildProgramLinkMessage("Linked program", shaders, programLog));
			}
		} finally {
			if (!linked) {
				GL20.glDeleteProgram(program);
			}
		}

		return program;
	}

	private static Optional<String> getProgramLog(int program) {
		return Optional.ofNullable(StringUtils.stripToNull(GL20.glGetProgramInfoLog(program, GL20.glGetProgrami(program, GL_INFO_LOG_LENGTH))));
	}

	private static String buildProgramLinkMessage(String message, IntList shaders, Optional<String> programLog) {
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		if (!shaders.isEmpty()) {
			sb.append("\n");
			for (int i = 0; i < shaders.size(); i++) {
				int shader = shaders.getInt(i);
				sb.append(typeName(GL20.glGetShaderi(shader, GL_SHADER_TYPE))).append(":");
				sb.append("\n");
				sb.append(GL20.glGetShaderSource(shader, GL20.glGetShaderi(shader, GL_SHADER_SOURCE_LENGTH)));
				if (i + 1 < shaders.size()) {
					sb.append("\n");
				}
			}
		}
		if (programLog.isPresent()) {
			sb.append("\n");
			sb.append("Log:").append("\n").append(programLog.get());
		}
		return sb.toString();
	}

	private static String typeName(int type) {
		switch (type) {
		case GL_FRAGMENT_SHADER:
			return "Fragment Shader";
		case GL_VERTEX_SHADER:
			return "Vertex Shader";
		case GL_GEOMETRY_SHADER:
			return "Geometry Shader";
		case GL_TESS_EVALUATION_SHADER:
			return "Tesselation Evaluation Shader";
		case GL_TESS_CONTROL_SHADER:
			return "Tesselation Control Shader";
		case GL_COMPUTE_SHADER:
			return "Compute Shader";
		default:
			return "Unkown (" + type + ")";
		}
	}

	public void use() {
		GL20.glUseProgram(this.shaderProgram);
	}

	public void delete() {
		GL20.glDeleteProgram(this.shaderProgram);
	}

	public int getShaderProgram() {
		return this.shaderProgram;
	}

	public static class Builder {

		private final Int2ObjectMap<Supplier<String>> shaderMap = new Int2ObjectOpenHashMap<>();

		public ShaderProgram.Builder addShader(int type, Supplier<String> source) {
			this.shaderMap.put(type, source);
			return this;
		}

		public ShaderProgram build() throws ShaderException {
			return new ShaderProgram(this.shaderMap);
		}

	}

}
