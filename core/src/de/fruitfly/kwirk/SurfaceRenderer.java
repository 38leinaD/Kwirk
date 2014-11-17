package de.fruitfly.kwirk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class SurfaceRenderer {
	private int primitiveType;
	private int vertexIdx;
	private int numSetTexCoords;
	private final int maxVertices;
	private int numVertices;

	private final Mesh mesh;
	private ShaderProgram shader;
	private boolean ownsShader;
	private final int numTexCoords;
	private final int vertexSize;
	private final int normalOffset;
	private final int colorOffset;
	private final int texCoordOffset;
	private final Matrix4 modelMatrix = new Matrix4();
	private final Matrix4 viewMatrix = new Matrix4();
	private final Matrix4 projectionMatrix = new Matrix4();
	private final float[] vertices;
	private final String[] shaderUniformNames;

	public SurfaceRenderer(ShaderProgram shader) {
		this.maxVertices = 50000;
		this.numTexCoords = 1;
		this.shader = shader;

		VertexAttribute[] attribs = buildVertexAttributes(true,
				true, numTexCoords);
		mesh = new Mesh(false, maxVertices, 0, attribs);

		vertices = new float[maxVertices
				* (mesh.getVertexAttributes().vertexSize / 4)];
		vertexSize = mesh.getVertexAttributes().vertexSize / 4;
		normalOffset = mesh.getVertexAttribute(Usage.Normal) != null ? mesh
				.getVertexAttribute(Usage.Normal).offset / 4 : 0;
		colorOffset = mesh.getVertexAttribute(Usage.ColorPacked) != null ? mesh
				.getVertexAttribute(Usage.ColorPacked).offset / 4 : 0;
		texCoordOffset = mesh.getVertexAttribute(Usage.TextureCoordinates) != null ? mesh
				.getVertexAttribute(Usage.TextureCoordinates).offset / 4 : 0;

		shaderUniformNames = new String[numTexCoords];
		for (int i = 0; i < numTexCoords; i++) {
			shaderUniformNames[i] = "u_sampler" + i;
		}
	}

	private VertexAttribute[] buildVertexAttributes(boolean hasNormals,
			boolean hasColor, int numTexCoords) {
		Array<VertexAttribute> attribs = new Array<VertexAttribute>();
		attribs.add(new VertexAttribute(Usage.Position, 3,
				ShaderProgram.POSITION_ATTRIBUTE));
		if (hasNormals)
			attribs.add(new VertexAttribute(Usage.Normal, 3,
					ShaderProgram.NORMAL_ATTRIBUTE));
		if (hasColor)
			attribs.add(new VertexAttribute(Usage.ColorPacked, 4,
					ShaderProgram.COLOR_ATTRIBUTE));
		for (int i = 0; i < numTexCoords; i++) {
			attribs.add(new VertexAttribute(Usage.TextureCoordinates, 2,
					ShaderProgram.TEXCOORD_ATTRIBUTE + i));
		}
		VertexAttribute[] array = new VertexAttribute[attribs.size];
		for (int i = 0; i < attribs.size; i++)
			array[i] = attribs.get(i);
		return array;
	}

	public void setShader(ShaderProgram shader) {
		if (ownsShader)
			this.shader.dispose();
		this.shader = shader;
		ownsShader = false;
	}

	public void begin(Matrix4 modelMatrix, Matrix4 viewMatrix, Matrix4 projectionMatrix, int primitiveType) {
		this.modelMatrix.set(modelMatrix);
		this.viewMatrix.set(viewMatrix);
		this.projectionMatrix.set(projectionMatrix);
		this.primitiveType = primitiveType;
	}

	public void color(Color color) {
		vertices[vertexIdx + colorOffset] = color.toFloatBits();
	}

	public void color(float r, float g, float b, float a) {
		vertices[vertexIdx + colorOffset] = Color.toFloatBits(r, g, b, a);
	}

	public void texCoord(float u, float v) {
		final int idx = vertexIdx + texCoordOffset;
		vertices[idx + numSetTexCoords] = u;
		vertices[idx + numSetTexCoords + 1] = v;
		numSetTexCoords += 2;
	}

	public void normal(float x, float y, float z) {
		final int idx = vertexIdx + normalOffset;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;
	}

	public void vertex(float x, float y, float z) {
		final int idx = vertexIdx;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;

		numSetTexCoords = 0;
		vertexIdx += vertexSize;
		numVertices++;
	}

	public void flush() {
		if (numVertices == 0)
			return;
		shader.begin();
		shader.setUniformMatrix("u_modelMatrix", modelMatrix);
		shader.setUniformMatrix("u_viewMatrix", viewMatrix);
		shader.setUniformMatrix("u_projectionMatrix", projectionMatrix);
		for (int i = 0; i < numTexCoords; i++)
			shader.setUniformi(shaderUniformNames[i], i);
		mesh.setVertices(vertices, 0, vertexIdx);
		mesh.render(shader, primitiveType);
		shader.end();

		numSetTexCoords = 0;
		vertexIdx = 0;
		numVertices = 0;
	}

	public void end() {
		flush();
	}

	public int getNumVertices() {
		return numVertices;
	}

	public int getMaxVertices() {
		return maxVertices;
	}

	public void dispose() {
		if (ownsShader && shader != null)
			shader.dispose();
		mesh.dispose();
	}
}
