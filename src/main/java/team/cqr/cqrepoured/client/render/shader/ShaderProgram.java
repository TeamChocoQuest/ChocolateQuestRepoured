package team.cqr.cqrepoured.client.render.shader;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class ShaderProgram {

	private static final Logger LOGGER = LogManager.getLogger();
	private int shaderProgram;

	private ShaderProgram(Int2ObjectMap<Supplier<String>> shaderMap) {
		this.shaderProgram = glCreateProgram();

		IntList shaderList = new IntArrayList();
		for (Int2ObjectMap.Entry<Supplier<String>> entry : shaderMap.int2ObjectEntrySet()) {
			int shader = glCreateShader(entry.getIntKey());
			glShaderSource(shader, entry.getValue().get());
			glCompileShader(shader);

			String log = glGetShaderInfoLog(shader, Integer.MAX_VALUE);
			if (!log.isEmpty()) {
				LOGGER.info(log);
			}
			int compileStatus = glGetShaderi(shader, GL_COMPILE_STATUS);
			if (compileStatus != GL11.GL_TRUE) {
				throw new RuntimeException("Failed to compile shader: " + compileStatus);
			}

			shaderList.add(shader);
		}

		shaderList.forEach(shader -> glAttachShader(this.shaderProgram, shader));
		glLinkProgram(this.shaderProgram);

		String log = glGetProgramInfoLog(this.shaderProgram, Integer.MAX_VALUE);
		if (!log.isEmpty()) {
			LOGGER.info(log);
		}
		int linkStatus = glGetProgrami(this.shaderProgram, GL_LINK_STATUS);
		if (linkStatus != GL11.GL_TRUE) {
			throw new RuntimeException("Failed to link programm: " + linkStatus);
		}

		shaderList.forEach(GL20::glDeleteShader);
	}

	public void use() {
		glUseProgram(this.shaderProgram);
	}

	public void delete() {
		glDeleteProgram(this.shaderProgram);
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

		public ShaderProgram build() {
			return new ShaderProgram(this.shaderMap);
		}

	}

}
