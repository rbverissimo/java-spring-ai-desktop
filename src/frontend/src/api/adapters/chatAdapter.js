/**
 * Anti-Corruption Layer (ACL) Adapter for Chat Responses.
 * Normalizes backend responses into a consistent frontend-friendly message format.
 */
export function adaptChatHistory(rawData) {
  if (!rawData) {
    return [];
  }

  // Handle case where backend returns a direct array
  let rawMessages = [];
  if (Array.isArray(rawData)) {
    rawMessages = rawData;
  } else if (typeof rawData === 'object') {
    // Handle object wrappers like { messages: [...] } or { data: [...] } or { content: [...] }
    rawMessages = rawData.messages || rawData.data || rawData.content || [];
  }

  if (!Array.isArray(rawMessages)) {
    return [];
  }

  return rawMessages.map((msg, index) => {
    // Handle cases where messages might be plain strings or objects
    if (typeof msg === 'string') {
      return {
        role: 'assistant',
        content: msg
      };
    }

    const role = msg.role || msg.type || msg.messageType || 'assistant';
    const content = msg.content || msg.text || msg.message || '';

    // Normalize role string if needed
    let normalizedRole = 'assistant';
    const lowerRole = String(role).toLowerCase();
    if (lowerRole.includes('user')) {
      normalizedRole = 'user';
    } else if (lowerRole.includes('error')) {
      normalizedRole = 'error';
    } else if (lowerRole.includes('assistant') || lowerRole.includes('ai') || lowerRole.includes('bot')) {
      normalizedRole = 'assistant';
    }

    return {
      key: msg.id || index,
      role: normalizedRole,
      content: String(content)
    };
  });
}
