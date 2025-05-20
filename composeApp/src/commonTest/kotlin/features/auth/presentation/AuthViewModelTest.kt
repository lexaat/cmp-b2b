package features.auth.presentation

import app.cash.turbine.test
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*
import kotlinx.coroutines.flow.first
import io.mockk.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: AuthViewModel

    @BeforeTest
    fun setup() {
        authRepository = mockk()
        tokenManager = mockk(relaxed = true)
        viewModel = AuthViewModel(authRepository, tokenManager, testScope)
    }

    @Test
    fun `successful login without OTP or password change navigates to main`() = testScope.runTest {
        // given
        val fakeToken = "token123"
        coEvery { authRepository.login("user", "pass") } returns AuthResponse(
            requireOtp = false,
            requirePasswordChange = false,
            token = fakeToken
        )

        // when
        viewModel.dispatch(AuthIntent.SubmitCredentials("user", "pass"))

        // then
        viewModel.sideEffect.test {
            assertEquals(AuthSideEffect.NavigateToMain, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        assertEquals(false, viewModel.isLoading.first())
        coVerify { tokenManager.saveToken(fakeToken) }
    }

    @Test
    fun `login that requires password change sets state`() = testScope.runTest {
        coEvery { authRepository.login(any(), any()) } returns AuthResponse(
            requireOtp = false,
            requirePasswordChange = true,
            token = ""
        )

        viewModel.dispatch(AuthIntent.SubmitCredentials("user", "pass"))

        assertEquals(AuthState.RequirePasswordChange, viewModel.state.first())
    }

    @Test
    fun `error during login emits error side effect`() = testScope.runTest {
        coEvery { authRepository.login(any(), any()) } throws RuntimeException("Ошибка логина")

        viewModel.dispatch(AuthIntent.SubmitCredentials("fail", "bad"))

        viewModel.sideEffect.test {
            val effect = awaitItem()
            assertTrue(effect is AuthSideEffect.ShowError)
            assertEquals("Ошибка логина", (effect as AuthSideEffect.ShowError).message)
            cancelAndConsumeRemainingEvents()
        }
    }
}
