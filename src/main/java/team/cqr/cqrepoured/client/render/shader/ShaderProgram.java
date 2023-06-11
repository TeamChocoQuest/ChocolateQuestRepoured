package team.cqr.cqrepoured.client.render.shader;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.function.IntConsumer;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL20.*;

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

			int logLength = glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH);
			if (logLength > 0) {
				LOGGER.info(() -> glGetShaderInfoLog(shader, logLength));
			}
			int compileStatus = glGetShaderi(shader, GL_COMPILE_STATUS);
			if (compileStatus != GL11.GL_TRUE) {
				throw new RuntimeException("Failed to compile shader: " + compileStatus);
			}

			shaderList.add(shader);
		}

		shaderList.forEach(new IntConsumer() {
			
			@Override
			public void accept(int value) {
				glAttachShader(ShaderProgram.this.shaderProgram, value);
			}
		});
		glLinkProgram(this.shaderProgram);

		int logLength = glGetProgrami(this.shaderProgram, GL20.GL_INFO_LOG_LENGTH);
		if (logLength > 0) {
			LOGGER.info(() -> glGetProgramInfoLog(this.shaderProgram, logLength));
		}
		int linkStatus = glGetProgrami(this.shaderProgram, GL_LINK_STATUS);
		if (linkStatus != GL11.GL_TRUE) {
			throw new RuntimeException("Failed to link programm: " + linkStatus);
		}

		shaderList.forEach(new IntConsumer() {
			
			@Override
			public void accept(int value) {
				GL20.glDeleteShader(value);
			}
		});
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
