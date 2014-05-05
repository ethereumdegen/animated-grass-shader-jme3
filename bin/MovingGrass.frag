
#ifdef TEXTURE
uniform sampler2D m_Texture;
varying vec2 texCoord;
#endif
 
uniform vec4 m_Color;
 
void main(void)
{
#ifdef TEXTURE
vec4 texVal = texture2D(m_Texture, texCoord);
gl_FragColor = texVal * m_Color;
#else
gl_FragColor = m_Color;
#endif
}