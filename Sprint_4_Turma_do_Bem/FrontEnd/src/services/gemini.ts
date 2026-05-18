// Groq API — llama-3.1-8b-instant
// Documentação: https://console.groq.com/docs/openai

const GROQ_KEY = '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'
const GROQ_URL = 'https://api.groq.com/openai/v1/chat/completions'

export async function gerarDescricaoClinica(anotacoes: string): Promise<string> {
  const resp = await fetch(GROQ_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${GROQ_KEY}`,
    },
    body: JSON.stringify({
      model: 'llama-3.1-8b-instant',
      temperature: 0.3,
      max_tokens: 250,
      messages: [
        {
          role: 'system',
          content: 'Você é um assistente odontológico da ONG Turma do Bem. Gere descrições clínicas profissionais e objetivas para prontuários odontológicos. Use linguagem técnica simples, em português, com no máximo 3 frases curtas. Não invente procedimentos que não foram mencionados. Responda apenas com o texto do prontuário, sem introdução ou explicação.',
        },
        {
          role: 'user',
          content: `Anotações do dentista: "${anotacoes}"`,
        },
      ],
    }),
  })

  if (!resp.ok) {
    const err = await resp.json()
    throw new Error(err?.error?.message ?? 'Erro ao chamar a IA')
  }

  const data = await resp.json()
  return data.choices?.[0]?.message?.content?.trim() ?? ''
}
