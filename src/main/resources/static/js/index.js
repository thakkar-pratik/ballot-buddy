document.addEventListener('DOMContentLoaded', () => {
    // Interactive Voting Guide Logic
    window.showStep = function(stepNumber) {
        // Hide all contents
        document.querySelectorAll('.step-content').forEach(content => {
            content.classList.add('hidden');
        });
        
        // Deactivate all buttons
        document.querySelectorAll('.step-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        
        // Show selected
        document.getElementById(`step-${stepNumber}`).classList.remove('hidden');
        document.querySelectorAll('.step-btn')[stepNumber - 1].classList.add('active');
    };

    window.pressEVMButton = function(id) {
        playBeep();
        
        // Light up the specific LED
        const led = document.getElementById(`led-${id}`);
        led.classList.add('active');
        
        setTimeout(() => {
            alert("🗳️ BEEP! Your vote for Candidate " + id + " has been cast.");
            showStep(4);
            led.classList.remove('active');
        }, 800);
    };

    function playBeep() {
        const audioCtx = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioCtx.createOscillator();
        const gainNode = audioCtx.createGain();

        oscillator.type = 'sine';
        oscillator.frequency.setValueAtTime(2000, audioCtx.currentTime); // High pitch beep
        gainNode.gain.setValueAtTime(0.1, audioCtx.currentTime);

        oscillator.connect(gainNode);
        gainNode.connect(audioCtx.destination);

        oscillator.start();
        setTimeout(() => oscillator.stop(), 600); // 0.6s beep length
    }

    fetchTimeline();
    fetchStates();
    setupChat();
    setupChatToggle();
});

async function fetchTimeline() {
    const timelineContainer = document.getElementById('timeline');
    try {
        const response = await fetch('/api/election/timeline');
        const data = await response.json();
        
        timelineContainer.innerHTML = '';
        data.steps.forEach(step => {
            const item = document.createElement('div');
            item.className = 'timeline-item';
            item.setAttribute('role', 'listitem');
            
            item.innerHTML = `
                <div class="timeline-content">
                    <span class="timeline-date">${step.date}</span>
                    <h3>${step.title}</h3>
                    <p>${step.description}</p>
                    ${step.completed ? '<span style="color: var(--success-green); font-weight: bold;">✓ Completed</span>' : ''}
                </div>
            `;
            timelineContainer.appendChild(item);
        });
    } catch (error) {
        console.error('Error fetching timeline:', error);
    }
}

async function fetchStates() {
    const stateGrid = document.getElementById('state-grid');
    try {
        const response = await fetch('/api/election/states');
        const data = await response.json();
        
        stateGrid.innerHTML = '';
        data.states.forEach(state => {
            const card = document.createElement('div');
            card.className = 'state-card';
            card.innerHTML = `
                <h3>${state.stateName}</h3>
                <div class="state-info"><strong>Voters:</strong> ${state.voterCount.toLocaleString()}</div>
                <div class="state-info"><strong>Major Parties:</strong> ${state.parties}</div>
                <div class="state-info"><strong>Key Participants:</strong> ${state.mainParticipants}</div>
                <div class="state-info"><strong>Next Election:</strong> ${state.electionDate}</div>
                <div class="state-info"><strong>Status:</strong> ${state.currentStatus}</div>
            `;
            stateGrid.appendChild(card);
        });
    } catch (error) {
        console.error('Error fetching states:', error);
        stateGrid.innerHTML = '<p style="color: red;">Failed to load state intelligence data.</p>';
    }
}

function setupChatToggle() {
    const container = document.getElementById('chat-container');
    const toggleBtn = document.getElementById('chat-toggle');
    const closeBtn = document.getElementById('chat-close');

    toggleBtn.addEventListener('click', () => container.classList.remove('minimized'));
    closeBtn.addEventListener('click', () => container.classList.add('minimized'));
}

function setupChat() {
    const chatForm = document.getElementById('chat-form');
    const chatInput = document.getElementById('chat-input');

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const query = chatInput.value.trim();
        if (!query) return;

        appendMessage(query, 'user');
        chatInput.value = '';

        const loadingId = 'loading-' + Date.now();
        appendMessage('Thinking...', 'ai', loadingId);

        try {
            const response = await fetch('/api/chat/ask', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ query })
            });
            const data = await response.json();
            
            const loadingEl = document.getElementById(loadingId);
            if (loadingEl) loadingEl.remove();
            appendMessage(data.response, 'ai');
        } catch (error) {
            console.error('Chat error:', error);
            const loadingEl = document.getElementById(loadingId);
            if (loadingEl) loadingEl.remove();
            appendMessage('Sorry, I encountered an error. Please try again.', 'ai');
        }
    });
}

function appendMessage(text, sender, id = null) {
    const chatMessages = document.getElementById('chat-messages');
    const msgDiv = document.createElement('div');
    msgDiv.className = `message ${sender}`;
    if (id) msgDiv.id = id;
    msgDiv.textContent = text;
    chatMessages.appendChild(msgDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}
